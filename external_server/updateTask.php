<?php
include 'db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
//$EUID, $Type, $Title, $Location, $Date, $UserID, $Description, $Time, $Complete
if(isset($_POST['EUID'])&&isset($_POST['Type'])&&isset($_POST['Title'])&&isset($_POST['Location'])&&isset($_POST['Date'])&&isset($_POST['UserID'])&&isset($_POST['Description'])&&isset($_POST['Time'])&&isset($_POST['Complete'])){
	$EUIDe = $_POST['EUID'];
	$Type = $_POST['Type'];
	$Title = $_POST['Title'];
	$Location = $_POST['Location'];
	$Date = $_POST['Date'];
  $UserID = $_POST['UserID];
  $Description = $_POST['Description'];
  $Time = $_POST['Time'];
  $Complete = $_POST['Complete'];
	
	//Query to update a movie
	$query = "UPDATE tasks SET Type=?,Title=?,Location=?,Date=?,UserID=?,Description=?,Time=?,Complete=? WHERE EUID=?";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//Bind parameters
		$stmt->bind_param("issssiss",$EUID,$Type,$Title,$Location,$Date,$UserID,$Description,$Time,$Complete);
		//Exceting MySQL statement
		$stmt->execute();
		//Check if data got updated
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Task successfully updated";
			
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "Task not found";
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
