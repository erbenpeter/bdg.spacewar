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

<h1>Regisztráció</h1>
<form method="post" action="." name="registerform">

    <table border="0">
		<tr>
			<td>Felhasználónév (számok, betûk,<br /> pont és kötõjel; 2-64 karakter hosszú):</td>
			<!-- the user name input field uses a HTML5 pattern check -->
			<td><input id="login_input_username" class="login_input" type="text" pattern="[a-zA-Z0-9\.\-\_]{2,64}" name="user_name" required /></td>
		<tr>
		<tr>
			<td>Email cím:</td>
			<!-- the email input field uses a HTML5 email type check -->
			<td><input id="login_input_email" class="login_input" type="email" name="user_email" required /></td>
		</tr>
		<tr>
			<td>Jelszó (legalább 6 karakter):</td>
			<td><input id="login_input_password_new" class="login_input" type="password" name="user_password_new" pattern=".{6,}" required autocomplete="off" /></td>
		</tr>
		<tr>
			<td>Jelszó ismét:</td>
			<td><input id="login_input_password_repeat" class="login_input" type="password" name="user_password_repeat" pattern=".{6,}" required autocomplete="off" /></td>
		</tr>
		<tr>
			<td />
			<td><input type="submit"  name="register" value="Regisztráció" /></td>
		</tr>
	</table>

</form>

<a href="<?php echo PAGE_FULL_URL; ?>/login">Már regisztráltál? Vissza a bejelentkezéshez!</a>

<?php
include "footer.php";
?>