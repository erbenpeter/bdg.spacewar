<?php

include "../views/header.php";
?>
<script type="text/javascript" src="<?php echo PAGE_FULL_URL; ?>/scripts/BdgSpacewar.js"></script>
<script type="text/javascript">
	window.addEventListener("load",function() {
		Game.fromCsvUrl("<?php echo $csvUrl; ?>").startDrawing('canvas');
	});
</script>
<canvas id="canvas"></canvas>
<?php

include "../views/footer.php";

?>