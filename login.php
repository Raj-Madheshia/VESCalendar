<?php
$db = "vescad";
$user = "root";
$pass = "";
$host ="localhost";

$email = $_GET['email'];
$password = $_GET['password'];

$conn = mysqli_connect($host, $user, $pass, $db);

if($conn)
{
	$q = "Select * from users where email like '$email' and pass like '$password'";
	$result = mysqli_query($conn,$q);
	
	if(mysqli_num_rows($result) > 0)
	{
		$row = mysqli_fetch_array($result);
		$data = $row[0];
		echo "'$data'";
	}
	else{
		echo "Login Failed";
	}
}
else{
	echo "Not Connected";
}
?>