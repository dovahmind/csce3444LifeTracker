<?php

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

/* Calculating the date's weekday name, and then only including the first 
 * three characters in lowercase to match the habits schema */
$weekday = date('l', strtotime($datestr));
$weekday = strtolower(substr($weekday, 0, 3));


/* DEBUGGING! DISPLAYING WHAT WE GOT */
//echo ('Here is the weekday in short form: ' . $weekday);

/* ADDITIONAL CODE ADDED BY JACOB ROQUEMORE ENDS HERE */

/* CHANGE BY JACOB ROQUEMORE. Rather than using the original $query found in 
 * fetchingAllTasks.php, the query will try to select from the reminder and
 * habits table using a left join based on conditions dependent on either the date,
 * or if a habit occurs on the same weekday as the user's date */ 

$query = "SELECT reminders.rid, reminders.type, reminders.title, reminders.date,
reminders.description, reminders.start_time, reminders.end_time,
reminders.completed FROM reminders LEFT JOIN habits ON reminders.rid =
habits.rid WHERE reminders.uid = {$uidint} AND (reminders.date = ? OR
habits.{$weekday} = 1);";

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
	$stmt->bind_param('s', $datestr);
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	/* CHANGE BY JACOB ROQUEMORE HERE. REMOVED THE RETURN OF LOCATION AND EUID,
	 * or user id, and added return of rid and type, as well as start_time, 
	 * and end_time and changed complete to checkboxResource. All of these changes
	 * where made to more closely match the object design in DailyTask.java in
	 * our Android code */
	$stmt->bind_result($rid, $type, $Title, $Date, $Description, $start_time, $end_time, 	     $checkboxResource);

	/* CHANGE BY JACOB HERE. CHANGED movieArray to tasksArray, and changed the 
	 * keys of the array in quotations ("") to match the binded variables above */
	//Fetch 1 row at a time	
	while($stmt->fetch()){
		//Populate the task array
		$tasksArray["rid"] = $rid;
		$tasksArray["type"] = $type;
    		$tasksArray["Title"] = $Title;
  		$tasksArray["Date"] = $Date;
    		$tasksArray["Description"] = $Description;
    		$tasksArray["start_time"] = $start_time;
		$tasksArray["end_time"] = $end_time;
    		$tasksArray["checkboxResource"] = $checkboxResource;
    		$result[]=$tasksArray;
	}
	$stmt->close();
	/*CHANGE MADE BY JACOB HERE. REMOVED THE SUCCESS KEY AND DATA KEYS FROM
	 *THE ARRAY AND JUST ADDING THE RESULT TO THE RESPONSE ARRAY TO MAKE
	 *DECODING JSON DATA EASIER */
	$response = $result;
	//Originally: $response[] = $result;
	
 
}else{
	//Some error while fetching data
	$errormsg = mysqli_error($con);
	$response = "error with mysql: " . $errormsg;

	/* ORIGINALLY IN fetchingAllTasks.php:
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
	 */
		
	
}
//Display JSON response
echo json_encode($response);
 
?>
