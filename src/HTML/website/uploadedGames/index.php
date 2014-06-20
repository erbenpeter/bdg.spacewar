<?php
include "../config/config.php";
include "../views/header.php";

?>

<h1>Feltöltött játékok</h1>
<table border="0">
	<tr>
		<th>1. játékos</th>
		<th>2. játékos</th>
		<th>Feltöltõ</th>
		<th>Feltöltés ideje</th>
	</tr>
<?php	

$con = mysqli_connect(DB_HOST,DB_USER,DB_PASS);
mysqli_select_db($con,DB_NAME);

$gamesResult = mysqli_query($con,"SELECT `id` ,`player1` ,`player2` ,`date` ,`uploader` FROM `uploaded_games`");
while($row = mysqli_fetch_array($gamesResult)) {
	$userRow = mysqli_fetch_array(mysqli_query($con,"SELECT `user_name` FROM `users` WHERE `user_id`='" . $row["uploader"] . "';"));
	$user_name = $userRow["user_name"];
	
	$link = "showGame.php?id=" . $row["id"];
	echo "<tr><td><a href=\"$link\">" . $row["player1"] . "</a></td>" .
		"<td><a href=\"$link\">" . $row["player2"] . "</a></td>" .
		"<td><a href=\"$link\">" . $user_name . "</a></td>" .
		"<td><a href=\"$link\">" . $row["date"] . "</a></td></tr>";
}
?>
</table>
<?php

include "../views/footer.php";
?>