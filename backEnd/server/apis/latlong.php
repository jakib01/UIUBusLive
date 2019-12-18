<?php
include_once('../common/include.php');
include_once('../common/encipher.php');
$user = json_decode(file_get_contents("php://input"));
if(!$user->busId){
    sendResponse(400, [] , 'Bus ID Required !');  
}else{
    $conn=getConnection();
    if($conn==null){
        sendResponse(500,$conn,'Server Connection Error !');
    }else{
        $password=doEncrypt($user->password);
        $sql = "SELECT * from live_locations where bus_id = '".$user->busId."'";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            $users=array();
            while($row = $result->fetch_assoc()) {
                $user=array(
                    "id" =>  $row["id"],
                    "lat" => $row["latitude"],
                    "long" => $row["longitude"]
                );
                array_push($users,$user);
            }
            sendResponse(200,$users,'Live Details');
        } else {
            sendResponse(404,[],'Live detail not available');
        }
        $conn->close();
    }
}
?>