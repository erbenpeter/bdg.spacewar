package bdgspacewardisplay;

import java.awt.Color;

abstract class SpaceObject {
    abstract void draw(Draw D);
}

class Spaceship extends SpaceObject {
    int id; double x, y, vx, vy;
    double f;
    Spaceship(int id, double params[]) {
        this.id = id;
        this.x = params[0];
        this.y = params[1];
        this.vx = params[2];
        this.vy = params[3];
        this.f = Math.toRadians(params[4]);
    }
    
    @Override
    public void draw(Draw D) {
        D.setPenColor(Color.BLUE);
        D.filledCircle(x, y, 50);
        D.setPenColor();
    }
}

class Bullet extends SpaceObject {
    int id; double x, y, vx, vy;
    Bullet(int id, double params[]) {
        this.id = id;
        this.x = params[0];
        this.y = params[1];
        this.vx = params[2];
        this.vy = params[3];
    }
    
    @Override
    public void draw(Draw D) {
        D.setPenColor(Color.BLACK);
        D.filledCircle(x, y, 5);
        D.setPenColor();
    }
}

class Explosion extends SpaceObject {
    int id; double x, y, ttl;
    Explosion(int id, double params[]) {
        this.id = id;
        this.x = params[0];
        this.y = params[1];
        this.ttl = params[2];
    }
    
    @Override
    public void draw(Draw D) {
        D.setPenColor(Color.RED);
        D.filledCircle(x, y, 55);
        D.setPenColor();
    }
}

class Planet extends SpaceObject {
    int id; double x, y, r, m;
    Planet(int id, double params[]) {
        this.id = id;
        this.x = params[0];
        this.y = params[1];
        this.r = params[2];
        this.m = params[3];
    }
    
    @Override
    public void draw(Draw D) {
        D.setPenColor(Color.ORANGE);
        D.filledCircle(x, y, r);
        D.setPenColor();
    }
}

class World extends SpaceObject {
    int id; double bax, bay, jfx, jfy;
    World(int id, double params[]) {
        this.id = id;
        this.bax = params[0];
        this.bay = params[1];
        this.jfx = params[2];
        this.jfy = params[3];
    }
    
    @Override
    public void draw(Draw D) {
        
    }
}