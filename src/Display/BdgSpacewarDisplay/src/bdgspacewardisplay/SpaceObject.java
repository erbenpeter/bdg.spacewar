package bdgspacewardisplay;

import java.awt.Color;

abstract class SpaceObject {
    abstract void draw(Draw D, World W);
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
    public void draw(Draw D, World W) {
        D.setPenColor(Color.BLUE);
        D.filledCircle(x, y, W.r_spaceship);
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
    public void draw(Draw D, World W) {
        D.setPenColor(Color.BLACK);
        D.filledCircle(x, y, W.r_bullet);
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
    public void draw(Draw D, World W) {
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
    public void draw(Draw D, World W) {
        D.setPenColor(Color.ORANGE);
        D.filledCircle(x, y, r);
        D.setPenColor();
    }
}

class World extends SpaceObject {
    int id; double XMIN, YMIN, XMAX, YMAX; double r_spaceship, r_bullet;
    World(int id, double params[]) {
        this.id = id;
        this.XMIN = params[0];
        this.YMIN = params[1];
        this.XMAX = params[2];
        this.YMAX = params[3];
        this.r_spaceship = params[4];
        this.r_bullet = params[5];
    }
    
    @Override
    public void draw(Draw D, World W) {
        
    }
}