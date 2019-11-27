<?php 

session_start();

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

if(!isset($_SESSION['loggedin']))
{
	die('Error! Not logged in!');
}

else
{
	/* A variable that will store the reminder id from the main 
	 * reminder table if there is a reminder of a different type */
	$rid;

	/* Retrieving the user id from their session */
	$uid = $_SESSION['uid'];

	$uidint = intval($uid);	
	
	if(!isset($_POST['json_data']))
	{
		die("Error! Didn't send json_data parameter!");
	}

	/* Decoding the json received */
	$json = $_POST['json_data'];

	if($json == NULL)
	{
		die("Error! Didn't send any json_data!");

	}
	
	$jsonobj = json_decode($json);

	/* Assigning the main elements to variables for a query */
	$type = $jsonobj->{'type'};
	
	$title = $jsonobj->{'title'};

	$date = $jsonobj->{'date'};

	/* Removing the new line characters from date */
	$date = str_replace("\n", "", $date);

	/* Formatting the date to work with DateTime */
	$date = str_replace("\/", "-", $date);	
	
	$description = $jsonobj->{'description'};	

	if(isset($jsonobj->{'start_time'}))
	{
		$start_time = $jsonobj->{'start_time'};
	}

	else
	{
		$start_time = "00:00:00";
	}

	if(isset($jsonobj->{'end_time'}))
	{
		$end_time = $jsonobj->{'end_time'};
	}

	else
	{
		$end_time = "00:00:00";
	}	

	if(isset($jsonobj->{'completed'}))
	{
		$completed = $jsonobj->{'completed'};
	}

	else
	{
		$completed = 0;
	}

	$completedint = intval($completed);


	//if($type == "event")
	//{
	/* Make a prepared statement for just the REMINDERS table */
	if ($stmt = $conn->prepare('INSERT INTO reminders (uid, type, title, date, description, start_time, end_time, completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)')) 
	{	
		/* Binding all  ... */
		$stmt->bind_param('issssssi', $uidint, $type, $title, $date, $description, $start_time, $end_time, $completedint);

		$stmt->execute();

		$rid = $stmt->insert_id;

		
		echo 'reminder success! ';	
	} 

	else 
	{
		// Something is wrong with the sql statement, 
		// check to make sure accounts table exists with all 3 fields.
		die('Error! Could not prepare statement!');
	}
	//}

	/* If it was a reocurring task, then gather additional information here */
	if($type == "reocc")
	{
		/* If the reminder id could not be gathered from the initial 
		 * insertion into the reminders table, then terminate here */
		if($rid == null)
		{
			die('Error! Could not get reminder id from initial insertion!');
		}

		/* Gather the month_time_interval from the json */
		if(isset($jsonobj->{'month_time_int'}))
		{
			$month_time_int = $jsonobj->{'month_time_int'};
		}

		else
		{
			$month_time_int = 0;
		}

		/* Gather the weekly_time_interval from the json if it is available */
		if(isset($jsonobj->{'week_time_int'}))
		{
			$week_time_int = $jsonobj->{'week_time_int'};
		}

		else
		{
			$week_time_int = 0;
		}

		/* Gather daily_time_interval from the json if it is available */

		if(isset($jsonobj->{'day_time_int'}))
		{
			$day_time_int = $jsonobj->{'day_time_int'};
		}

		else
		{
			$day_time_int = 0;
		}

		/* Gather if it is a consumable item or not. (This will be 
		 * incorporated to additional functionality if we have time 
		 * before November 25th) */
		if(isset($jsonobj->{'consumable'}))
		{
			if($jsonobj->{'consumable'} == 1)
			{
				$consumable = 1;

				/* IF TIME GATHER ADDITIONAL INFORMATION FROM
				 * THE JSON AND MAKE AN AMAZON OR OTHER THIRD
				 * PARTY API REQUEST STRING TO STORE (BEFORE
				 * NOV 25) */
			}

			else
			{
				$consumable = 0;
			}
		}

		else
		{
			$consumable = 0;
		}

		/* Calculate the upcoming date based on the intervals and the 
		 * date provided by the user */

		
		/* Making a temporary date time object out of the date variable */
		$dt = new DateTime($date);

		/* Forming date arithmetic string from intervals */
		$dateadder = "$month_time_int" . " months" . "$week_time_int" . " weeks" . "$day_time_int" . " days";

		/* Adding the intervals */
		date_add($dt,date_interval_create_from_date_string($dateadder));


		/*Converting the date to a string for mysql */
		$upcom = $dt->format('Y-m-d');


		$prepare_string = 'INSERT INTO recurring_tasks (rid, month_time_int, week_time_int, day_time_int, consumable, upcom) VALUES (?, ?, ?, ?, ?, ?)'; 
		

		/* Then insert this into the reoccurring task table */
		if ($stmt = $conn->prepare($prepare_string)){
			/* Binding all  ... */
			$stmt->bind_param('iiiiis', $rid, $month_time_int, $week_time_int, $day_time_int, $consumable, $upcom);

			$stmt->execute();

			echo 'reoccuring task success!';		
		} 

		else 
		{
			// Something is wrong with the sql statement, 
			// check to make sure accounts table exists with all 3 fields.
			die('Error! Could not prepare statement!');
		}
	}	

	if($type == "habit")
	{
		/* If the reminder id could not be gathered from the initial 
		 * insertion into the reminders table, then terminate here */
		if($rid == null)
		{
			die('Error! Could not get reminder id from initial insertion!');
		}

		/* Gather the integer array from the json document */
		if(isset($jsonobj->{'day_names'}))
		{
			$dayarr = $jsonobj->{'day_names'};
		}

		else
		{
			die('Error! The days for the habit were not specified!');
		}	


		/* Forming a proper array from day arr */

		/* First removing the [ and ] characters */
		$dayarr = str_replace("[", "", $dayarr);
		$dayarr = str_replace("]", "", $dayarr);	


		/* Then using explode to get each element separated by a comma */
		$dayarr = explode(",", $dayarr);	

		/* Initial declaration of day values for database insertion */
		//$everyday = 0;
		$mon = 0;
		$tue = 0;
		$wed = 0;
		$thu = 0;
		$fri = 0;
		$sat = 0;
		$sun = 0;	

		/* Going through all possible values in the array (0th index = everyday, 
		 * 1 = monday, ..., 7 = sunday) and determining what the bool 
		 * val (reminder: 1 = true, 0 = false) should be set to */

		/* If everyday is set, then set monday through sunday to 1 */
		if($dayarr[0] == 1)
		{
			$mon = 1;
			$tue = 1;
			$wed = 1;
			$thu = 1;
			$fri = 1;
			$sat = 1;
			$sun = 1;
		}

		/* Otherwise, determine which days where set to true based on
		 * their position in the array */
		else
		{
			if($dayarr[1] == 1)
			{
				$mon = 1;
			}
				
			if($dayarr[2] == 1)
			{
				$tue = 1;

			}

			if($dayarr[3] == 1)
			{
				$wed = 1;

			}

			if($dayarr[4] == 1)
			{
				$thu = 1;

			}
				
			if($dayarr[5] == 1)
			{
				$fri = 1;

			}

			if($dayarr[6] == 1)
			{
				$sat = 1;

			}

			if($dayarr[7] == 1)
			{
				$sun = 1;

			}
		}

		$prepare_string = 'INSERT INTO habits (rid, mon, tue, wed, thu, fri, sat, sun) VALUES (?, ?, ?, ?, ?, ? , ?, ?)'; 

		/* Then insert this into the reoccurring task table */
		if ($stmt = $conn->prepare($prepare_string)){
			/* Binding all  ... */
			$stmt->bind_param('iiiiiiii', $rid, $mon, $tue, $wed, $thu, $fri, $sat, $sun);

			$stmt->execute();	

			if($stmt == 0)
			{
				die('Error! Could not execute statement!');
			}

			echo 'habit success!';
		} 

		else 
		{
			// Something is wrong with the sql statement, 
			// check to make sure accounts table exists with all 3 fields.
			die('Could not prepare statement!');
		}
	}
}

/* Closing the mysqli connection when we are done with it */
$conn->close();
?>
