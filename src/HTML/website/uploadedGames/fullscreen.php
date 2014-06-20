<!DOCTYPE html>
<html>
	<head>
		<title>bdg.spacewar</title>
		<style type="text/css">
			html, body, #canvas {
				margin: 0px;
				padding: 0px;
				width:100%;
				height:100%;
			}
		</style>
	</head>
	<body>
		<script type="text/javascript" src="<?php echo PAGE_FULL_URL; ?>/scripts/BdgSpacewar.js"></script>
		<script type="text/javascript" src="<?php echo PAGE_FULL_URL; ?>/scripts/BdgSpacewar_config.js.php"></script>
		<script type="text/javascript">
			function resizeHandler() {
				var container = document.body;
				var canvasObj = document.getElementById("canvas");
				canvasObj.width=container.offsetWidth;
				canvasObj.height=document.body.offsetHeight;
				
				/*var cell = document.getElementById("formCell");
				var formContainer = document.getElementById("formContainer");
				cell.height = formContainer.offsetHeight;*/
			}
			
			window.addEventListener("load",function() {
				Game.fromCsvUrl("<?php echo $csvUrl; ?>",function(gameObj) {
					gameObj.startDrawing('canvas');
				});
				resizeHandler();
			});
			window.addEventListener("resize",resizeHandler);
			
		</script>
		<canvas id="canvas">
		
		</canvas>
	</body>
</html>