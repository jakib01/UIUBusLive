<?php
include_once('../common/include.php');
include_once('../common/encipher.php');
$user = json_decode(file_get_contents("php://input"));
if(!$user->email){
    sendResponse(400, [] , 'Email Required !');  
}else if(!$user->password){
    sendResponse(400, [] , 'Password Required !');        
}else{
    $conn=getConnection();
    if($conn==null){
        sendResponse(500,$conn,'Server Connection Error !');
    }else{
        $password=doEncrypt($user->password);
        $sql = "SELECT u.id id, u.username username, u.email email, r.role role, rd.bus_id bus_id from users u left join roles r on u.role_id = r.id left join registered_datas rd on u.id = rd.user_id where u.email = '".$user->email."' and u.password = '".$password."' and u.status = 1";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            $users=array();
            while($row = $result->fetch_assoc()) {
                $user=array(
                    "id" =>  $row["id"],
                    "username" => $row["username"],
                    "email" => $row["email"],
                    "role" => $row["role"],
                    "bus_id" => $row["bus_id"]
                );
                array_push($users,$user);
            }
            sendResponse(200,$users,'User Details');
        } else {
            sendResponse(404,[],'User not available');
        }
        $conn->close();
    }
}
?>