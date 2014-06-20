<?php
require_once "../config/config.php";
require_once "../classes/Login.php";

$login = new Login();


if(!$login->isUserLoggedIn()) {
	header("Location: " . PAGE_FULL_URL . "/login/?redirect=" . urlencode("../upload/") . "&message=" . urlencode("A felt�lt�shez el�bb be kell l�pned."));
} else {


	include "../views/header.php";
	if(count($_POST)==0) {
		include "uploadForm.php";
	} else if(!isset($_POST["player1"]) || $_POST["player1"]=="") {
		echo "<div class=\"warning\">Hi�nyzik az 1. j�t�kos neve.</div>";
		include "uploadForm.php";
	} else if(!isset($_POST["player2"]) || $_POST["player2"]=="") {
		echo "<div class=\"warning\">Hi�nyzik a 2. j�t�kos neve.</div>";
		include "uploadForm.php";
	} else if(!isset($_FILES["file"])) {
		echo "<div class=\"warning\">Hi�nyzik a CSV.</div>";
		include "uploadForm.php";
	} else {
		$pattern = "/[^\\.\\-a-zA-z0-9������������������]/";
		$replace = "_";
		
		$player1 = preg_replace($pattern,$replace,$_POST["player1"]);
		$player2 = preg_replace($pattern,$replace,$_POST["player2"]);
		
		
		$con = mysqli_connect(DB_HOST,DB_USER,DB_PASS);
		mysqli_select_db($con, DB_NAME);
		
		mysqli_query($con, "INSERT INTO  `uploaded_games` (`id` ,`player1` ,`player2` ,`date` ,`uploader`)" .
						" VALUES (NULL ,  '" . $player1 . "',  '" . $player2 . "',  '" . date('Y-m-d H:i:s') . "', '" . $login->getUserId() . "');");
		
		$id = mysqli_insert_id($con);
		
		
		move_uploaded_file($_FILES["file"]["tmp_name"], "../uploadedGames/games/" . $id . ".txt");
		
		
		echo "Sikeres felt�lt�s :)";
		
		
		mysqli_close($con);

	}
	include "../views/footer.php";
}
?>