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

	/* Retrieving the user id from their session */
	$uid = $_SESSION['uid'];

	$uidint = intval($uid);	

	/* Decoding the json received */
	$json = $_POST['json_data'];

	if($json == NULL)
	{
		die("Error! Didn't send json_data!");

	}
	
	$jsonobj = json_decode($json);

	/* Assigning the main elements to variables for a query */
	$type = $jsonobj->{'type'};
	
	$title = $jsonobj->{'title'};

	$date = $jsonobj->{'date'};
	
	$description = $jsonobj->{'description'};	

	if($jsonobj->{'start_time'} != NULL)
	{
		$start_time = $jsonobj->{'start_time'};
	}

	else
	{
		$start_time = "00:00:00";
	}

	if($jsonobj->{'end_time'} != NULL)
	{
		$end_time = $jsonobj->{'end_time'};
	}

	else
	{
		$end_time = "00:00:00";
	}	

	$completed = $jsonobj->{'completed'};

	$completedint = intval($completed);


	/* FOCUSING ON SINGLE EVENTS FOR NOW */
	if($type == "event")
	{
		/* Make a prepared statement for just the REMINDERS table */
		if ($stmt = $conn->prepare('INSERT INTO reminders (uid, type, title, date, description, start_time, end_time, completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)')) 
		{	
			/* Binding all  ... */
			$stmt->bind_param('issssssi', $uidint, $type, $title, $date, $description, $start_time, $end_time, $completedint);

			$stmt->execute();
			
			echo 'Successfully inserted reminder!';	
		} 

		else 
		{
			// Something is wrong with the sql statement, 
			// check to make sure accounts table exists with all 3 fields.
			echo 'Could not prepare statement!';
		}
	}

	/* WHEN ADDING OTHER TASKS USE TRANSACTIONS AND PREPARED STATEMENTS!
	 * MORE INFO HERE: 
	 * https://stackoverflow.com/questions/5178697/mysql-insert-into-multiple-tables-database-normalization
	 * AND MORE INFO HERE:
	 * https://stackoverflow.com/questions/5124546/select-last-insert-id-returns-0-after-using-prepared-statement?rq=1
	 */
}

/* Closing the mysqli connection when we are done with it */
$conn->close();
?>
