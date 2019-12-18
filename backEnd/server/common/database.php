<?php
    function getConnection()
    {
        // $host = 'localhost';
        // $db_name = 'uiulivebus';
        // $username = 'root';
        // $password = '';

        //live
        $host = 'wasdpa-bd.org';
        $db_name = 'uiulivebus_new';
        $username = 'wasdpabd';
        $password = 'UFi1]K04sbd6C;';
        
        $conn= new mysqli($host, $username, $password, $db_name);
        if ($conn->connect_error) {
            $conn= null;
        }
        return $conn;
    }
?>