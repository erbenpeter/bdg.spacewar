<?php
include "../config/config.php";
if(!isset($_GET["id"]) || $_GET["id"]=="") {
	include "../views/header.php";
	echo "No de mi a játék id-je?!";
	include "../views/footer.php";
	die();
}

$id = intval($_GET["id"]);

$csvUrl = PAGE_FULL_URL . "/uploadedGames/games/" . $id . ".txt";

include "fullscreen.php";

?>