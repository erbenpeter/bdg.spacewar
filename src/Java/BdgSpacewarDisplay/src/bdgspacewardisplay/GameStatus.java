package bdgspacewardisplay;

import java.util.Locale;
import java.util.Scanner;

class GameStatus {
    static final int TYPE_SPACESHIP = 0,
	TYPE_BULLET = 1,
	TYPE_EXPLOSION = 2,
	TYPE_PLANET = 3,
	TYPE_WORLD = 4;
    int t;
    int status;
    int n; // objektumok száma
    SpaceObject obj[];
    
    GameStatus(String s) {
        Scanner sc = new Scanner(s);
        sc.useLocale(Locale.UK);
        t = sc.nextInt();
        status = sc.nextInt();
        n = sc.nextInt();
        obj = new SpaceObject[n];
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            int type = sc.nextInt();
            int pnum = sc.nextInt();
            double params[] = new double[pnum];
            for (int j = 0; j < pnum; j++) params[j] = sc.nextDouble();
            switch (type) {
                case TYPE_SPACESHIP: obj[i] = new Spaceship(id, params); break;
                case TYPE_BULLET: obj[i] = new Bullet(id, params); break;
                case TYPE_EXPLOSION: obj[i] = new Explosion(id, params); break;
                case TYPE_PLANET: obj[i] = new Planet(id, params); break;
                case TYPE_WORLD: obj[i] = new World(id, params); break;
                default: break;
            }
        }
        sc.close();
    }
    
    void draw(Draw D) {
        for (SpaceObject so : obj) {
            so.draw(D);
        }
    }
    
    void drawMap(Draw D) {
        for (SpaceObject so : obj) {
            if (so instanceof Planet || so instanceof World) so.draw(D);
        }
    }
}

