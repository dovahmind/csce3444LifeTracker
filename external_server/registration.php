<?php
/*The following code is based on the, "register.php", code example from 
 * codeshack at the following url: 
 * https://codeshack.io/secure-registration-system-php-mysql/ */

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

if(mysqli_connect_errno()) 
{
	/* Indicate that a connection could not be formed with the database, 
	 * and display an associated error message */
	die ('Could not connect to MYSQL. Check variable values, and check mysql status: ' . mysqli_connect_error());
}

/* If any of data needed for registration was not sent properly */
if(!isset($_POST['user_name'], $_POST['email'], $_POST['password']))
{
	//State that the user needed to send more data
	die('The user needs to at least register with a user_name, email, and password');
}

/* If any of the data in the fields was blank */
if(empty($_POST['user_name'] ||	$_POST['email'] || $_POST['password']))
{
	/* Then state the user cannot leave any fields blank */
	die('Please make sure the user_name, email, and paassword fields are not left blank!');
}

/* Checking to see if an account with the same username and password exists */
/* Using prepared statements for security reasons */
if($stmt = $conn->prepare('SELECT uid, password FROM users WHERE user_name = ? OR email = ?'))
{
	/* Validating the user's username on the server side before making any sql
	 * queries or adding the user to the database */

	/* THe following is based on both the, "Invalid Characters Validation"
	 * section from: https://codeshack.io/secure-registration-system-php-mysql/, 
	 * as well as the first answer from user Ish at the following
	 * stackoverflow link:
	 * https://stackoverflow.com/questions/4383878/php-username-validation
	 * */

	if(preg_match('/[A-Za-z0-9_-]+/', $_POST['user_name']) == 0)
	{
		die('This is not a valid username!');
	}

	/*Making sure the email is valid */
	if(!filter_var($_POST['email'], FILTER_VALIDATE_EMAIL))
	{
		die('This is not a valid email!');
	}

	/* IF NECESSARY PUT PASSWORD VALIDATION TECHNIQUES HERE FROM BOTH 
	 * codeshack source, and "PHP:Creating Secure Website" linkedin learning
	 * course (ask group members) */

	/* BINDING THE POST PARAMETERS TO THE PREPARED STATEMENT */
	/* NOTE THAT SINCE email and username are varchar in the table, we 
	 * will use strings for the binding */
	$stmt->bind_param('ss', $_POST['user_name'], $_POST['password']);

	/* Executing the prepared statement */
	$stmt->execute();

	/* Storing the query result from the executed statement */
	$stmt->store_result();

	/* If a user with either the same username or password was found, then
	 * tell the user that their was a registration error in vague terms and
	 * ask them to enter a different username and email. It is kept vague
	 * for security reasons */
	if($stmt->num_rows > 0)
	{
		echo 'Registration error! Please try using a different username or password';
	}

	/* Otherwise, if the user does not exist, then make an account for them
	 * */
	else
	{
		/* Ask group members if we still want support for full names. If
			* we do then add a nested if statement below here
			* stating that if the full name post parameters was not
			* empty then put the full name into a prepared stmt.
			* Then put the code below into a separate nested else if
			* */

		/* Also ask if we want an email activation system or not */
		if ($stmt = $conn->prepare('INSERT INTO users (email, user_name, password) VALUES (?, ?, ?)')) 
		{
			/* Hashing the password using the bcrypt algorithm (and 
			 * salting it through php's salting method attached to 
			 * password_hash) */
			$password = password_hash($_POST['password'], PASSWORD_DEFAULT);
			/* Binding all  ... */
			$stmt->bind_param('sss', $_POST['email'], $_POST['user_name'], $password);
			$stmt->execute();
			echo 'You have successfully registered, you can now login!';
		} 
		else 
		{
			// Something is wrong with the sql statement, check to make sure accounts table exists with all 3 fields.
			echo 'Could not prepare statement!';
		}


	}
}

/* Otherwise something went wrong with the sql SELECT statement. Double check
 * table schema before further development */
else
{
	echo 'Error in the registration process! Developer, check the registration code';
}

/* Closing the mysqli connection when the user has been registered */
$conn->close();
?>
