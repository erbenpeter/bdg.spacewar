var WORLD_WIDTH = 1000, WORLD_HEIGHT = 1000;


var BACKGROUND_IMAGE_PATH = "background.jpg"

var SPACESHIP_IMAGE_PATH = "spaceship.png";
	SPACESHIP_IMAGE_WIDTH = 128,
	SPACESHIP_IMAGE_HEIGHT = 128;
	
var BULLET_RADIUS = 5;

var EXPLOSION_IMAGE_PATH = "explosion.png",
	EXPLOSION_IMAGE_WIDTH = 64,
	EXPLOSION_IMAGE_HEIGHT = 64;

var PLANET_IMAGE_PATH = "planet.png";


var TYPE_SPACESHIP = 0,
	TYPE_BULLET = 1,
	TYPE_EXPLOSION = 2,
	TYPE_PLANET = 3,
	TYPE_WORLD = 4;



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


function GameState(t, status, objects) {
	this.t = t;
	this.status = status;
	this.objects = objects;
}
GameState.prototype.draw = function(canvasID) {
	var canvasObj = document.getElementById(canvasID);
	
	
	var width = canvasObj.width;
	var height = canvasObj.height;
	
	var ratio1 = WORLD_WIDTH/width, ratio2 = WORLD_HEIGHT/height;
	var ratio = ratio1>ratio2 ? ratio1 : ratio2;
	
	var context = canvasObj.getContext("2d");
	context.clearRect(0,0,width,height);
	
	context.scale(1/ratio,1/ratio);
	context.strokeRect(1,1,WORLD_WIDTH-1,WORLD_HEIGHT-1);
	
	
	var bgRatio1 = BdgSpacewar.backgroundImage.width/WORLD_WIDTH, bgRatio2 = BdgSpacewar.backgroundImage.height/WORLD_HEIGHT;
	var bgRatio = bgRatio1<bgRatio2 ? bgRatio1 : bgRatio2;
	context.drawImage(
			BdgSpacewar.backgroundImage,
			0,
			0,
			WORLD_WIDTH*bgRatio,
			WORLD_HEIGHT*bgRatio,
			0,
			0,
			WORLD_WIDTH,
			WORLD_HEIGHT
			)
	
	
	
	
	for(var i in this.objects) {
		var object = this.objects[i];
		object.draw(context);
	}
	context.scale(ratio,ratio);
}
GameState.fromCSV=function(csv) {
	var values_string = csv.split(" ");
	var values = new Array();
	
	for(var string in values_string)
		values.push( parseFloat(values_string[string]) );
	
	var t = values[0];
	var status = values[1];
	var n = values[2];
	var objects = new Array();
	var index = 3;
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
	return new GameState(t,status,objects);
}

function SpaceShip(id,params) {
	this.id=id;
	this.type=TYPE_SPACESHIP;
	this.x=params[0];
	this.y=params[1];
	this.vx=params[2];
	this.vy=params[3];
	this.ax=params[4];
	this.ay=params[5];
	this.d=Math.toRadians(params[6]);
}
SpaceShip.prototype.draw=function(context) {
	context.translate(this.x,this.y);
	context.rotate(this.d);
	context.drawImage(
				BdgSpacewar.spaceshipImage,
				-SPACESHIP_IMAGE_WIDTH/2,
				-SPACESHIP_IMAGE_HEIGHT/2,
				SPACESHIP_IMAGE_WIDTH,
				SPACESHIP_IMAGE_HEIGHT);
	context.rotate(-this.d);
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
	this.y1=params[3]
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




Math.toRadians=function(deg) {
	return deg/180*Math.PI;
}
Math.toDegrees=function(rad) {
	return rad*180/Math.PI;
}
