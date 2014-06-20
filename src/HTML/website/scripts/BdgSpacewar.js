/** 
  * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  * config.js.php must be included in order to use BdgSpacewar
  * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  */


var TYPE_SPACESHIP = 0,
	TYPE_BULLET = 1,
	TYPE_EXPLOSION = 2,
	TYPE_PLANET = 3,
	TYPE_WORLD = 4;
	
var STATUS_START = 0,
	STATUS_RUNNING = 1,
	STATUS_DRAWN = 2,
	STATUS_WON_1 = 3,
	STATUS_WON_2 = 4,
	STATUS_ERROR = 5;
	
var MESSAGE_DRAWN = "Döntetlen!",
	MESSAGE_WON_1 = "Az 1. játékos nyert.",
	MESSAGE_WON_2 = "A 2. játékos nyert.",
	MESSAGE_ERROR = "Hiba!";

var FPS = 30;
	



function BdgSpacewar() {}

BdgSpacewar.loadListeners = new Array();

BdgSpacewar.numberOfLoadingImages=4;
BdgSpacewar.spaceshipImage=null;
BdgSpacewar.explosionImage=null;
BdgSpacewar.planetImage=null;

BdgSpacewar.init=function() {
	var onload = function() {
		if(--BdgSpacewar.numberOfLoadingImages==0) BdgSpacewar.loadCompleted();
	}
	
	BdgSpacewar.spaceshipImage=new Image();
	BdgSpacewar.spaceshipImage.onload=onload;
	BdgSpacewar.spaceshipImage.src=SPACESHIP_IMAGE_PATH;
	
	BdgSpacewar.explosionImage=new Image();
	BdgSpacewar.explosionImage.onload=onload;
	BdgSpacewar.explosionImage.src=EXPLOSION_IMAGE_PATH;
	
	BdgSpacewar.planetImage=new Image();
	BdgSpacewar.planetImage.onload=onload;
	BdgSpacewar.planetImage.src=PLANET_IMAGE_PATH;
	
	BdgSpacewar.backgroundImage=new Image();
	BdgSpacewar.backgroundImage.onload=onload;
	BdgSpacewar.backgroundImage.src=BACKGROUND_IMAGE_PATH;
}
BdgSpacewar.addEventListener=function(event, callback) {
	switch(event) {
	case "load":
		BdgSpacewar.loadListeners.push(callback);
		break;
	}
}
BdgSpacewar.loadCompleted=function() {
	for(var i in BdgSpacewar.loadListeners)
		BdgSpacewar.loadListeners[i]();
}


window.addEventListener("load",BdgSpacewar.init);




function Game(gameConfiguration,states) {
	this.gameConfiguration=gameConfiguration;
	this.states=states;
}
// ["canvasId" : intervalId1, ...]
Game.intervals = {};
// ["canvasId" : progress1, ...]
Game.progress = {};


Game.fromCsv=function(csv) {
	var values_string = csv.split(/\s+/);
	var values = new Array();
	
	for(var string in values_string)
		values.push( parseFloat(values_string[string]) );
	
	
	this.index=0;
	var states = new Array();
	var conf;
	var lastObj;
	do {
		lastObj = GameState.fromValues(values,this);
		if(lastObj==null) break;
		if(lastObj.status==STATUS_START) conf = lastObj;
		else states.push(lastObj);
	} while(lastObj.status==STATUS_RUNNING || lastObj.status==STATUS_START);
	
	return new Game(new GameConfiguration(conf),states);
}



/**
  * Constructs a new BdgSpacewar object, using the CSV-formatted data on the
  * given url. Due to the Javascript concurrency model, this function will not
  * return the object, but will call the given handler function, and passes the
  * constructed object as the first parameter.
  * @param url The url of the CSV data
  * @param handler The handler function. The new BdgSpacewar object will be passed
  *		   as first parameter.
  * @param params This object will be passed to the handler function as the second
  * argument.
  */
Game.fromCsvUrl=function(url, handler, params) {
	new UrlLoader(url,
		function(csv, params) {
			params[0](Game.fromCsv(csv),params[1]);
		},
		[handler,params]);
}

Game.prototype.startDrawing=function(canvasId) {
	clearInterval(Game.intervals[canvasId]);
	this.canvasId = canvasId;
	Game.intervals[canvasId] = setInterval(function(game) {
										game.drawNext(game.canvasId);
									},1000/FPS,this);
	Game.progress[canvasId] = 0;
}
Game.stopDrawing=function(canvasId) {
	clearInterval(Game.intervals[canvasId]);
	delete Game.intervals[canvasId];
	delete Game.progress[canvasId];
}
Game.prototype.drawNext=function(canvasId) {

	
	if(!this.states[Game.progress[canvasId]]) {
		Game.stopDrawing(canvasId);
		return;
	}
	
	
	this.states[Game.progress[canvasId]++].draw(canvasId,this.gameConfiguration);
	
}





/**
  * Converts the gameState object to GameConfiguration object
  */
function GameConfiguration(gameState) {
	this.planets = new Array();

	for(var i in gameState.objects) {
		var obj = gameState.objects[i];
		if(obj instanceof World) {
			this.world = obj;
		} else if(obj instanceof Planet) {
			this.planets.push(obj);
		}
	}
}



function GameState(t, status, objects) {
	this.t = t;
	this.status = status;
	this.objects = objects;
	/*var isDimensionDefined = false;
	for(var i=0;i<configuration.objects;i++)
		if(configuration.objects[i] instanceof World) {
			isDimensionDefined = true;
			this.x0 = configuration.objects[i].x0;
			this.x1 = configuration.objects[i].x1;
			this.y0 = configuration.objects[i].y0;
			this.y1 = configuration.objects[i].y1;
		}
	if(!isDimensionDefined) throw "The dimension of the GameState object has not been defined in configuration";*/
}
GameState.prototype.draw = function(canvasId,gameConfiguration) {
	var canvasObj = document.getElementById(canvasId);
	
	
	var width = canvasObj.width;
	var height = canvasObj.height;
	
	
	var worldWidth = gameConfiguration.world.x1-gameConfiguration.world.x0;
	var worldHeight = gameConfiguration.world.y1-gameConfiguration.world.y0;
	
	var ratio1 = worldWidth/width, ratio2 = worldHeight/height;
	var ratio = ratio1>ratio2 ? ratio1 : ratio2;
	
	var bgRatio1 = BdgSpacewar.backgroundImage.width/worldWidth,
		bgRatio2 = BdgSpacewar.backgroundImage.height/worldHeight;
		
	var bgRatio = bgRatio1<bgRatio2 ? bgRatio1 : bgRatio2;
	
	var context = canvasObj.getContext("2d");
	
	context.clearRect(0,0,width,height);
	
	context.scale(1/ratio,1/ratio);
	
	
	context.drawImage(
			BdgSpacewar.backgroundImage,
			0,
			0,
			worldWidth*bgRatio,
			worldHeight*bgRatio,
			0,
			0,
			worldWidth,
			worldHeight
			);
	
	
	context.translate(-gameConfiguration.world.x0,-gameConfiguration.world.y0);
	
	var drawText=function(context,text,gameConfiguration) {
		context.fillStyle="#CC0";
		context.textAlign="center";
		context.font="30px Arial";
		context.fillText(text,
						worldWidth/ratio/2,
						worldHeight/ratio/2);
	}
	
	if(this.status!=STATUS_RUNNING) {
		context.translate(gameConfiguration.world.x0,gameConfiguration.world.y0);
		context.scale(ratio,ratio);
	}
	switch(this.status) {
		case STATUS_RUNNING:
	
			for(var i in gameConfiguration.planets) {
				var planet = gameConfiguration.planets[i];
				planet.draw(context);
			}
			
			
			
			
			for(var i in this.objects) {
				var object = this.objects[i];
				object.draw(context, gameConfiguration);
			}
			break;
		case STATUS_DRAWN:
			drawText(context,MESSAGE_DRAWN,gameConfiguration);
			break;
		case STATUS_WON_1:
			drawText(context,MESSAGE_WON_1,gameConfiguration);
			break;
		case STATUS_WON_2:
			drawText(context,MESSAGE_WON_2,gameConfiguration);
			break;
		default:
			drawText(context,MESSAGE_ERROR,gameConfiguration);
	}
	if(this.status==STATUS_RUNNING) {
		context.translate(gameConfiguration.world.x0,gameConfiguration.world.y0);
		context.scale(ratio,ratio);
	}
}
/**
  * This should be only called, if we want to draw only this GameState, because it
  * will draw background.
  * @param csv The csv in string format
  */
GameState.fromCsv=function(csv) {
	var values_string = csv.split(/\s+/);
	var values = new Array();
	
	for(var string in values_string)
		values.push( parseFloat(values_string[string]) );
	
	return GameState.fromValues(values, null);
}
/**
  * The CSV has been splitted into array of floats.
  * @param values The array of the floats
  * @param game	Optional. The Game object, which holdes the starting index.
  * 			This will be updated.
  */
GameState.fromValues=function(values, game) {
	try {
		var index = game ? game.index : 0;
		var t = values[index++];
		var status = values[index++];
		var n = values[index++];
		var objects = new Array();
		for(var i=0;i<n;i++) {
			var id = values[index++];
			var type = values[index++];
			var paramNumber = values[index++];
			var params = new Array();
			for(var j=0;j<paramNumber;j++) params.push(values[index++]);
			var object;
			switch(type) {
				case TYPE_SPACESHIP:
					object = new SpaceShip(id,params)
					break;
				case TYPE_BULLET:
					object = new Bullet(id,params);
					break;
				case TYPE_EXPLOSION:
					object = new Explosion(id,params);
					break;
				case TYPE_PLANET:
					object = new Planet(id,params);
					break;
				case TYPE_WORLD:
					object = new World(id,params);
					break;
				default:
					object = new UnknownObject(id,type,params);
					break;
			}
			objects[i] = object;
		}
		
		if(values.length<index-1) return null;
		
		if(game) game.index=index;
		return new GameState(t,status,objects);
		
		
	} catch(e) {}
	return null;
}





function SpaceShip(id,params) {
	this.id=id;
	this.type=TYPE_SPACESHIP;
	this.x=params[0];
	this.y=params[1];
	this.vx=params[2];
	this.vy=params[3];
	this.d=params[4];
}
SpaceShip.prototype.draw=function(context, gameConfiguration) {
	context.translate(this.x,this.y);
	context.rotate(this.d+Math.PI);
	context.drawImage(
				BdgSpacewar.spaceshipImage,
				-gameConfiguration.world.spaceshipRadius/2,
				-gameConfiguration.world.spaceshipRadius/2,
				gameConfiguration.world.spaceshipRadius,
				gameConfiguration.world.spaceshipRadius);
	context.rotate(-this.d-Math.PI);
	context.translate(-this.x,-this.y);
}



function Bullet(id,params) {
	this.id=id;
	this.type=TYPE_BULLET;
	this.x=params[0];
	this.y=params[1];
	this.vx=params[2];
	this.vy=params[3];
	this.ax=params[4];
	this.ay=params[5];
}
Bullet.prototype.draw=function(context) {
	context.beginPath();
	context.arc(this.x,this.y,BULLET_RADIUS,0,2*Math.PI);
	context.closePath();
	context.fillStyle="#CC0000";
	context.fill();
}



function Explosion(id,params) {
	this.id=id;
	this.type=TYPE_EXPLOSION;
	this.x=params[0];
	this.y=params[1];
	this.ttl=params[2];
}
Explosion.prototype.draw=function(context) {
	context.drawImage(
			BdgSpacewar.explosionImage,
			this.x-EXPLOSION_IMAGE_WIDTH/2,
			this.y-EXPLOSION_IMAGE_HEIGHT/2,
			EXPLOSION_IMAGE_WIDTH,
			EXPLOSION_IMAGE_HEIGHT);
}


function Planet(id,params) {
	this.id=id;
	this.type=TYPE_PLANET;
	this.x=params[0];
	this.y=params[1];
	this.r=params[2];
	this.m=params[3];
}
Planet.prototype.draw=function(context) {
	context.drawImage(
			BdgSpacewar.planetImage,
			this.x-this.r/2,
			this.y-this.r/2,
			this.r,
			this.r);
}


function World(id,params) {
	this.id=id;
	this.type=TYPE_WORLD;
	this.x0=params[0];
	this.y0=params[1]
	this.x1=params[2];
	this.y1=params[3];
	this.spaceshipRadius=params[4];
	this.bulletRadius=params[5];
}
World.prototype.draw=function(context) {

}




function UnknownObject(id,type,params) {
	this.id=id;
	this.type=type;
	this.params=params;
}
UnknownObject.prototype.draw=function(context) {
}




function UrlLoader(url, handler, params) {
	this.ajax = new XMLHttpRequest();
	this.ajax.urlLoader = this;
	this.handler=handler;
	this.params=params;
	
	this.ajax.onreadystatechange=function() {
		if(this.readyState==4 && this.status==200) {
			this.urlLoader.handler(this.responseText,this.urlLoader.params);
		}
	}
	
	this.ajax.open("GET",url);
	this.ajax.send();
}