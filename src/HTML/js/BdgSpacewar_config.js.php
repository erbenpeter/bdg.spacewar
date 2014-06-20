<?php
header("Content-Type: application/javascript");
include "../config/config.php";
?>
var BACKGROUND_IMAGE_PATH = "<?php echo PAGE_FULL_URL; ?>/style/spacewar_background.png"

var SPACESHIP_IMAGE_PATH = "<?php echo PAGE_FULL_URL; ?>/style/spaceship.png";
	
var BULLET_RADIUS = 5;

var EXPLOSION_IMAGE_PATH = "<?php echo PAGE_FULL_URL; ?>/style/explosion.png",
	EXPLOSION_IMAGE_WIDTH = 64,
	EXPLOSION_IMAGE_HEIGHT = 64;

var PLANET_IMAGE_PATH = "<?php echo PAGE_FULL_URL; ?>/style/planet.png";