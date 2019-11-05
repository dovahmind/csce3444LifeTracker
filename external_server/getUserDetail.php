<?php
include 'db/db_connect.php';
$userArray = array();
$response = array();
//Check for mandatory parameter movie_id
if(isset($_GET['UID'])){
	$UID = $_GET['UID'];
	//Query to fetch movie details
	$query = "SELECT email, username, fullname FROM users WHERE UID=?";
	if($stmt = $con->prepare($query)){
		//Bind UID parameter to the query
		$stmt->bind_param("i",$UID);
		$stmt->execute();
		//Bind fetched result to variables $email, $username, and $fullname
		$stmt->bind_result($email, $username, $fullname);
		//Check for results		
		if($stmt->fetch()){
			//Populate the movie array
			$userArray["UID"] = $UID;
			$userArray["email"] = $email;
     			$userArray["username"] = $username;
      			$userArray["fullname"] = $fullname;
      			$response["success"] = 1;
			$response["data"] = $userArray;
		
		
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "User not found";
		}
		$stmt->close();
 
 
	}else{
		//Whe some error occurs
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}
 
}else{
	//When the mandatory parameter movie_id is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter UID";
}
//Display JSON response
echo json_encode($response);
?>
