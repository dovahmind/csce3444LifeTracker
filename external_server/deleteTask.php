<?php
include 'db/db_connect.php';
$response = array();
//Check for mandatory parameter movie_id
if(isset($_POST['EUID'])){
	$EUID = $_POST['EUID'];
	$query = "DELETE FROM tasks WHERE EUID=?";
	if($stmt = $con->prepare($query)){
		//Bind EUID parameter to the query
		$stmt->bind_param("i",$EUID);
		$stmt->execute();
		//Check if the movie got deleted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Task got deleted successfully";
			
		}else{
			$response["success"] = 0;
			$response["message"] = "Task not found";
		}					
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
 
}else{
	$response["success"] = 0;
	$response["message"] = "missing parameter EUID";
}
echo json_encode($response);
?>
