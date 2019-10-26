<?php
$db = "vescad";
$user = "root";
$pass = "";
$host ="localhost";

$id = $_GET['id'];

$conn = mysqli_connect($host, $user, $pass, $db);

if($conn)
{
	$q = "Select * from roles where user_id like '$id'";
	$results = mysqli_query($conn,$q);
	$Rids = array();
	while($row = mysqli_fetch_assoc($results)) {
  		$Rids[] = $row['rid'];
	}
	if(count($Rids)!=0){
		$json_array = array();
		foreach($Rids as $Rid){
			$q = "Select * from events where role_id like '$Rid'";
			$results = mysqli_query($conn,$q);
		
			while($row = mysqli_fetch_assoc($results))
			{
				$json_array[] = $row;
			}
		}
		echo(json_encode($json_array));
;
	}
	
}
else{
	echo "Not Connected";
}
?>