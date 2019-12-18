<?php
include_once('../common/include.php');
include_once('../common/encipher.php');
$user = json_decode(file_get_contents("php://input"));
if(!$user->opt){
    sendResponse(400, [] , 'Opt Required !');  
}else{
    $conn=getConnection();
    if($conn==null){
        sendResponse(500,$conn,'Server Connection Error !');
    }else{
        // $password=doEncrypt($user->password);
        $sql = "SELECT * FROM routes WHERE status = 1";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            $users=array();
            while($row = $result->fetch_assoc()) {
                $user=array(
                    "id" =>  $row["id"],
                    "name" => $row["name"],
                    "full_route" => $row["full_route"]
                );
                array_push($users,$user);
            }
            sendResponse(200,$users,'Routes Details');
        } else {
            sendResponse(404,[],'Routes not available');
        }
        $conn->close();
    }
}
?>