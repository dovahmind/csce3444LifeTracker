<?php

/*The following code is based on the logout.php example from codeshack at the following url: 
https://codeshack.io/secure-login-system-php-mysql/ */

/* Starting the session on the server side for a user */
session_start();

/* Destroying the session on both the user side and server side */
session_destroy();

echo 'Logout success'
?>
