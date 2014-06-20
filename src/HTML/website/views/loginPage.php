<?php
require_once "../config/config.php";
include "header.php";

// show potential errors / feedback (from registration object)
if(isset($_GET["message"])) {
	echo "<div class=\"message\">" . $_GET["message"] . "</div>";
}
if (isset($registration)) {
    if ($registration->errors) {
        foreach ($registration->errors as $error) {
            echo "<div class=\"error\">$error</div>";
        }
    }
    if ($registration->messages) {
        foreach ($registration->messages as $message) {
            echo "<div class=\"message\">$message</div>";
        }
    }
}
?>
<h1>Bejelentkez�s</h1>
<form method="post" action="index.php<?php if(isset($_GET["redirect"])) echo "?redirect=" . urlencode($_GET["redirect"]); ?>" name="loginform">

    <table border="0">
		<tr>
			<td>Felhaszn�l�n�v:</td>
			<td><input type="text" name="user_name" /></td>
		</tr>
		<tr>
			<td>Jelsz�:</td>
			<td><input type="password" name="user_password" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" name="login" value="Bejelentkez�s!" /></td>
		</tr>
	</table>
</form>

<a href="<?php echo PAGE_FULL_URL; ?>/register">Regisztr�ci�</a>
<?php
include "footer.php";
?>