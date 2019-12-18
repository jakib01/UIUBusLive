<?php

header("Access-Control-Allow-Origin: *");

//local
// $host = 'localhost';
// $db_name = 'uiulivebus';
// $username = 'root';
// $password = '';

//live
$host = 'wasdpa-bd.org';
$db_name = 'uiulivebus';
$username = 'wasdpabd';
$password = 'UFi1]K04sbd6C;';


$conn= new mysqli($host, $username, $password, $db_name);
if ($conn->connect_error) {
    $conn= null;
}
// return $conn;


$bus_id = $_GET['id'];
$latitude = $_GET['lat'];
$longitude = $_GET['long'];

$sql = "UPDATE live_locations SET latitude='{$longitude}', longitude='{$latitude}' WHERE id = '{$bus_id}'";


if ($conn->query($sql) === TRUE) {
    echo "Latitude & Longitude have Saved Successfully!";
} else {
    echo "Error:" . $sql . "<br>" . $conn->error;
}

$conn->close();

?>