<?php
include 'db/db_connect.php';
//Query to select EUID, Type, Title, Location, Date, UserID, Description,
//Time, and Complete
$query = "SELECT EUID, Type, Title, Location, Date, UserID, Description, Time, Complete FROM Tasks";
$result = array();
$tasksArray = array();
$response = array();
//Prepare the query
if($stmt = $con->prepare($query)){
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	$stmt->bind_result($EUID, $Type, $Title, $Location, $Date, $UserID, $Description, $Time, $Complete);
	//Fetch 1 row at a time					
	while($stmt->fetch()){
		//Populate the task array
		$movieArray["EUID"] = $EUID;
		$movieArray["Type"] = $Type;
    $movieArray["Title"] = $Title;
    $movieArray["Location"] = $Location;
    $movieArray["Date"] = $Date;
    $movieArray["UserID"] = $UserID;
    $movieArray["Description"] = $Description;
    $movieArray["Time"] = $Time;
    $movieArray["Complete"] = $Complete;
    $result[]=$tasksArray;
		
	}
	$stmt->close();
	$response["success"] = 1;
	$response["data"] = $result;
	
 
}else{
	//Some error while fetching data
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
		
	
}
//Display JSON response
echo json_encode($response);
 
?>
