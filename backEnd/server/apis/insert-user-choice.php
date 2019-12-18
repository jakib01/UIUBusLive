<?php
include_once('../common/include.php');
include_once('../common/encipher.php');

$user = json_decode(file_get_contents("php://input"));
if(!$user->userId){
    sendResponse(400, [] , $user);  
} else if(!$user->routeId){
    sendResponse(400, [] , 'routeId Required !');  
} else if(!$user->pickUpPointId){
    sendResponse(400, [] , 'pickUpPointId Required !');        
} else if(!$user->reminderPointId){
    sendResponse(400, [] , 'reminderPointId Required !');        
} else if(!$user->busId){
    sendResponse(400, [] , 'busId Required !');        
} else{
    
    $conn=getConnection();
    if($conn==null){
        sendResponse(500, $conn, 'Server Connection Error !');
    }else{
        $sql="INSERT INTO registered_datas(`user_id`, `route_id`, `pickup_point_id`, `reminder_point_id`, `bus_id`, `status`)";
        $sql .= "VALUES ('".$user->userId."','".$user->routeId."','".$user->pickUpPointId."','".$user->reminderPointId."','".$user->busId."',";
        $sql .= " 1)";
        $result = $conn->query($sql);
        if ($result) {
            sendResponse(200, $result , 'User Data saved Successful.');
        } else {
            sendResponse(404, [] ,'User Data Registered');
        }
        $conn->close();
    }
}
?>