<?php

/*The following code is based on the home.php example from codeshack at the following url: 
https://codeshack.io/secure-login-system-php-mysql/ */

/* Starting the session on the server side for a user */
session_start();

/* If the user is not logged in, or does not have a session cookie on their device, then echo an error */
if(!isset($_SESSION['loggedin']))
{
	echo 'Not logged in!';
}

/* Otherwise if they are logged in, then display their username */
elseif(isset($_SESSION['loggedin']))
{
	echo 'User ' . $_SESSION['user_name'] . ' is logged in.';
}

/* Otherwise something went wrong, so send a generic error message */
else
{
	echo 'Double check the authentication logic!';
}
?>
