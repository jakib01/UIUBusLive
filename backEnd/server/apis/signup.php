<?php
include_once('../common/include.php');
include_once('../common/encipher.php');

$user = json_decode(file_get_contents("php://input"));
if(!$user->username){
    sendResponse(400, [] , $user);  
}else if(!$user->email){
    sendResponse(400, [] , 'Email Required !');  
}else if(!$user->password){
    sendResponse(400, [] , 'Password Required !');        
}else{
    $password = doEncrypt($user->password);
    $conn=getConnection();
    if($conn==null){
        sendResponse(500, $conn, 'Server Connection Error !');
    }else{
        $sql="INSERT INTO users(username, email, password, role_id, status)";
        $sql .= "VALUES ('".$user->username."','".$user->email."','";
        $sql .= $password."', 2, 1)";

        $result = $conn->query($sql);
        if ($result) {
            sendResponse(200, $result , 'User Registration Successful.');
        } else {
            sendResponse(404, [] ,'User not Registered');
        }
        $conn->close();
    }
}
?>