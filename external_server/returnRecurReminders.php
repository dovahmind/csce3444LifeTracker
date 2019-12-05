<?php

/* IMPORTANT NOTE: THIS IS A MODIFICATION OF returnDailyReminders.php that 
 * was written on 12-4 which simply adapts returnDailyReminders to work with
 * the schema for the recurringtasks table, and the general logic behind recurring
 * tasks */

/* ORIGINAL SCRIPT FROM HUY LE UNDER FETCHINGALLTASKS.PHP. ONLY SOME MINOR 
 * CHANGES HAVE BEEN MADE, WHICH ARE DESCRIBED IN THE COMMENTS */

//CHANGE BY JACOB ROQUEMORE. USED TO GATHER INFORMATION FROM USER SESSION COOKIE
session_start();

/* CHANGE BY JACOB ROQUEMORE STARTS HERE. this differs from include 'db/db_connect.php';
 * in original script */

/* Forming database connection */

/* IMPORTANT: IF DATABASE IS ON THE SAME SERVER AS PHP SCRIPT THEN LEAVE AS
 * 'localhost' OTHERWISE PUT THE HOSTNAME OR IP ADDRESS OF THE SERVER THAT
 * HOSTS A MYSQL SYSTEM */
$DATABASE_HOST = 'localhost';

/* IMPORTANT: REPLACE USER NAME WITH MYSQL USERNAME ON OWN SERVER
 * (can either be root or a custom user for mysql administration
 * purposes) */
$DATABASE_USER = '*****';


/* IMPORTANT: REPLACE PASSWORD WITH MYSQL PASSWORD ON OWN SERVER
 * (can either be root or a custom user for mysql administration
 * purposes) */
$DATABASE_PASS = '*****';

/* IMPORTANT: REPLACE DATABASE_NAME WITH THE NAME OF A DATABASE THAT CONTAINS
	* A USER ACCOUNT TABLE */
$DATABASE_NAME = '*****';

$conn = mysqli_connect($DATABASE_HOST, $DATABASE_USER, $DATABASE_PASS, $DATABASE_NAME);

if ( mysqli_connect_errno() ) {
	/* Indicate that a connection could not be formed with the database,
	 * and display an associated error message */
	die ('Could not connect to MYSQL. Check variable values, and check mysql status: ' . mysqli_connect_error());
}

/* CHANGE ENDS HERE */

/* ADDITIONAL CODE ADDED BY JACOB ROQUEMORE STARTS HERE */

if(!isset($_SESSION['loggedin']))
{
	die('Error! Not logged in!');
}

/* Getting the user's id and converting it to an int */
$uid = $_SESSION['uid'];
$uidint = intval($uid);

/* Making sure the user's device date from a post variable called inpdate exists,
 * and if it doesn't then terminate the php script */

if(!isset($_POST['inpdate']))
{
	die("Error! Didn't send inpdate parameter!");
}

$datestr = $_POST['inpdate'];

if($datestr == NULL)
{
	die("Error! Didn't send actual date for inpdate!");

}


/* DEBUGGING! DISPLAYING WHAT WE GOT */
//echo ('Here is the date: ' . $datestr);


/* DEBUGGING! DISPLAYING WHAT WE GOT */
//echo ('Here is the weekday in short form: ' . $weekday);

/* ADDITIONAL CODE ADDED BY JACOB ROQUEMORE ENDS HERE */

/* ADDITIONAL CHANGE BY JACOB ON 12-4. Gathering the daily habits that occur on
 * this weekday. If today's date is greater than the lastsync date 
 * found for a habit, then reset/update the completion status for that 
 * habit to 0 */
$gather = "SELECT reminders.rid, recurring_tasks.month_time_int, 
recurring_tasks.week_time_int, recurring_tasks.day_time_int, 
recurring_tasks.upcom FROM reminders INNER JOIN 
recurring_tasks ON reminders.rid = recurring_tasks.rid WHERE reminders.uid = {$uidint}";

$updater = "UPDATE reminders SET completed = 0 WHERE rid = ?";

$updatereocc = "UPDATE recurring_tasks SET upcom = ? WHERE rid = ?";

$currdate = strtotime($datestr);

if($stmt1 = $conn->prepare($gather)){
	/* CHANGE BY JACOB: Binding the date parameter to the query to prevent any potential 
	 * sql injection from the date POST parameter */
	$stmt1->execute();
	//Bind the fetched data to $movieId and $movieName
	/* CHANGE BY JACOB ROQUEMORE HERE. REMOVED THE RETURN OF LOCATION AND EUID,
	 * or user id, and added return of rid and type, as weel as start_time, 
	 * and end_time and changed complete to checkboxResource. All of these changes
	 * where made to more closely match the object design in DailyTask.java in
	 * our Android code */
	$rows = $stmt1->get_result();

	//$stmt1->close();
	
	if($rows->num_rows > 0)
	{
		while($row = $rows->fetch_assoc()){
			$tmprid = $row["rid"];	

			$objmonthint = $row["month_time_int"];

			$objweekint = $row["week_time_int"];

			$objdayint = $row["day_time_int"];

			$stored_date = $row["upcom"];

			//echo ("Stored date: " . $stored_date);
		
			$dateres = strtotime($stored_date);

			if($currdate > $dateres)
			{
				//echo ("Found a larger date!");
				//echo ("Rid is: " . $tmprid);
				//echo ("Prev date: " . $stored_date);
				//echo ("Current date: " . $datestr);
				if($stmt2 = $conn->prepare($updater)){
					$stmt2->bind_param('i', $tmprid);
					$stmt2->execute();
					$stmt2->close();
				
				} else{
					//Some error while fetching data
					$errormsg = mysqli_error($con);
					echo("error with mysql: " . $errormsg);

					/* ORIGINALLY IN fetchingAllTasks.php:
					$response["success"] = 0;
					$response["message"] = mysqli_error($con);
					 */
				}

				if($stmt3 = $conn->prepare($updatereocc)){
					/* Calculating new date based on intervals here */

					//Making a datetime object to perform calculations on
					$newdate = new DateTime($stored_date);

					//Forming date arithmetic string from intervals
					$dateadder = "$objmonthint" . " months" . "$objweekint" . " weeks" . "$objdayint" . " days";

					/* Adding the intervals */
					date_add($newdate,date_interval_create_from_date_string($dateadder));

					/*Converting the date to a string for mysql */
					$newdatestr = $newdate->format('Y-m-d'); 

					$stmt3->bind_param('si', $newdatestr, $tmprid);
					$stmt3->execute();
					$stmt3->close();
				
				} else{
					//Some error while fetching data
					$errormsg = mysqli_error($conn);
					echo("error with mysql: " . $errormsg);

					/* ORIGINALLY IN fetchingAllTasks.php:
					$response["success"] = 0;
					$response["message"] = mysqli_error($con);
					 */
				}
			}
		}
	}

	$rows->free();

	$stmt1->close();
}else{
	//Some error while fetching data
	$errormsg = mysqli_error($conn);
	echo("error with mysql: " . $errormsg);

	/* ORIGINALLY IN fetchingAllTasks.php:
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
	 */
}



/* CHANGE BY JACOB ROQUEMORE. Rather than using the original $query found in 
 * fetchingAllTasks.php, the query will try to select from the reminder and
 * habits table using a left join based on conditions dependent on either the date,
 * or if a habit occurs on the same weekday as the user's date */ 

$query = "SELECT reminders.rid, reminders.type, reminders.title, 
recurring_tasks.upcom, reminders.description, reminders.completed   
FROM reminders INNER JOIN recurring_tasks 
ON reminders.rid = recurring_tasks.rid WHERE reminders.uid = {$uidint};";


$result = array();
$tasksArray = array();
/* CHANGE BY JACOB. SINCE SUCCESS AND DATA KEYS ARE NO LONGER USED, RESPONSE 
 * WILL NOT BE AN ARRAY CONTAINING A SUBARRAY, BUT RATHER JUST CONTAIN THE RESULT
 * ARRAY. SO THIS HAS BEEN COMMENTED OUT FOR NOW */
//$response = array();
//Prepare the query
if($stmt = $conn->prepare($query)){
	/* CHANGE BY JACOB: Binding the date parameter to the query to prevent any potential 
	 * sql injection from the date POST parameter */
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	/* CHANGE BY JACOB ROQUEMORE HERE. REMOVED THE RETURN OF LOCATION AND EUID,
	 * or user id, and added return of rid and type, as weel as start_time, 
	 * and end_time and changed complete to checkboxResource. All of these changes
	 * where made to more closely match the object design in DailyTask.java in
	 * our Android code */
	$stmt->bind_result($rid, $type, $Title, $upcom, $Description, $checkboxResource);

	/* CHANGE BY JACOB HERE. CHANGED movieArray to tasksArray, and changed the 
	 * keys of the array in quotations ("") to match the binded variables above */
	//Fetch 1 row at a time	
	while($stmt->fetch()){
		//Populate the task array
		$tasksArray["rid"] = $rid;
		$tasksArray["type"] = $type;
    		$tasksArray["Title"] = $Title;
  		$tasksArray["upcom"] = $upcom;
    		$tasksArray["Description"] = $Description;
    		$tasksArray["checkboxResource"] = $checkboxResource;
    		$result[]=$tasksArray;
	}
	$stmt->close();
	/*CHANGE MADE BY JACOB! REMOVING THE SUCCESS KEY AND DATA KEYS FROM
	 *THE ARRAY AND JUST ADDING THE RESULT TO THE RESPONSE ARRAY TO MAKE
	 *DECODING JSON DATA EASIER */
	$response = $result;
	//Originally: $response[] = $result;
	
 
}else{
	//Some error while fetching data
	$errormsg = mysqli_error($conn);
	$response = "error with mysql: " . $errormsg;

	/* ORIGINALLY IN fetchingAllTasks.php:
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
	 */
		
	
}
//Display JSON response
echo json_encode($response);
 
?>
