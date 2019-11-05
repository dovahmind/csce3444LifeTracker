<?php
/*The following code is based on the, "authenticate.php" example code from the 
 * login system example at the following url: 
 * https://codeshack.io/secure-login-system-php-mysql/ */

/* Starting the session for a user on the server side */
session_start();

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

/* If a user either did not enter a username and password, or an email and
	* password, then terminate and state that the user needs to enter either
	* a user name or email and a password */
$usercombo = isset($_POST['user_name'], $_POST['password']);
$emailcombo = isset($_POST['email'], $_POST['password']);
$eitherset = ($emailcombo || $usercombo);

if ($eitherset == false) 
{
	// Stating error to user
	die ('Please provide either a username and password or an email and password');
}


// Checking to see if the user exists using an SQL Prepared Statement.
if ($stmt = $conn->prepare('SELECT uid, password, user_name FROM users WHERE user_name = ? OR email = ?')) 
{
	/* Checking for the existence of a user name or email by binding
		* user_name and email accordingly */
	$stmt->bind_param('ss', $_POST['user_name'], $_POST['email']);

	/* Executing the prepared SQL statement */
	$stmt->execute();

	// Storing the query result to check for account existence in the mysql
	// database
	$stmt->store_result();

	/* If a user was found with a matching user name or password */
	if ($stmt->num_rows > 0) 
	{
		/* Binding the user id, password, and user_name results from the prepared
		 * statement to the uid and password variables to work with in
		 * php */
		$stmt->bind_result($uid, $password, $user_name);

		/* Fetching the user id and password from the prepared statement
		 * query results */
		$stmt->fetch();

		/* Verifying the password using php's password verify function,
			* which works in tandem with the password_hash function
			* found in registration.php */
		if (password_verify($_POST['password'], $password)) 
		{
			/* If the password matched, then make a new session id
			 * for the user */
			session_regenerate_id();
			/* Set the user's logged in status with their session to
			 * true */
			$_SESSION['loggedin'] = TRUE;
			/* Set the associated username with their session */
			$_SESSION['user_name'] = $user_name;
			/* Set the associated userid with their session */
			$_SESSION['uid'] = $uid;
			/* Echoing a response indicating that the login was
				* successful, which can be used for determining
				* success on client/android side */
			echo 'Login success for: ' . $_SESSION['user_name'];
		} 

		/* For security purposes, echoing a vague login error message */
		else
		{
			echo 'Login error! Please try again!';
		}
	
	} 
	

	/* For security purposes, if the user could not be found it will echo a
		* vague login error message */
	else 
	{
		echo 'Login error! Please try again!';
	}
}

/* Closing the prepared statement */
$stmt->close();
?>
