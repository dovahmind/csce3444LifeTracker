<?php
include 'db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
//$email, $username, $fullname
if(isset($_POST['email'])&&isset($_POST['username'])&&isset($_POST['fullname'])){
	$UID = $_POST['UID'];
	$email = $_POST['email'];
	$username = $_POST['username'];
	$fullname = $_POST['fullname'];
	
	//Query to update a movie
	$query = "UPDATE user SET email=?,username=?,fullname=? WHERE UID=?";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//Bind parameters
		$stmt->bind_param("sss",$email, $username, $fullname);
		//Exceting MySQL statement
		$stmt->execute();
		//Check if data got updated
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "User successfully updated";
			
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "User not found";
		}					
	}else{
		//Some error while updating
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
 
}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);
?>
