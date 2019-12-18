<?php
include_once('../common/include.php');
include_once('../common/encipher.php');
$user = json_decode(file_get_contents("php://input"));
if(!$user->opt){
    sendResponse(400, [] , 'Opt Required !');  
}else if(!$user->route_id){
    sendResponse(400, [] , 'Route Required !');  
}else{
    $conn=getConnection();
    if($conn==null){
        sendResponse(500,$conn,'Server Connection Error !');
    }else{
        $route_id=$user->route_id;
        $sql = "SELECT * FROM `pickup_points` WHERE route_id = '".$route_id."' AND  status = 1";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            $users=array();
            while($row = $result->fetch_assoc()) {
                $user=array(
                    "id" =>  $row["id"],
                    "name" => $row["name"],
                    "location_name" => $row["location_name"]
                );
                array_push($users,$user);
            }
            sendResponse(200,$users,'Pick Up Point Details');
        } else {
            sendResponse(404,[],'Pick Up Point not available');
        }
        $conn->close();
    }
}
?>