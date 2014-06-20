<?php


if (version_compare(PHP_VERSION, '5.3.7', '<')) {
    exit("Sorry, Simple PHP Login does not run on a PHP version smaller than 5.3.7 !");
} else if (version_compare(PHP_VERSION, '5.5.0', '<')) {
    // if you are using PHP 5.3 or PHP 5.4 you have to include the password_api_compatibility_library.php
    // (this library adds the PHP 5.5 password hashing functions to older versions of PHP)
    require_once("../libraries/password_compatibility_library.php");
}


require_once "../config/config.php";
require_once "../classes/Login.php";


$login = new Login();

if($login->isUserLoggedIn()) {
	if(isset($_GET["redirect"])) $redirect = $_GET["redirect"];
	else $redirect = PAGE_FULL_URL;
	
	header("Location: " . $redirect);
} else {
	include "../views/loginPage.php";
}
?>