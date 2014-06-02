package bdg.spacewar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import bdg.spacewar.Obj.ObjMoving.*;

public class SpaceWar {
	public static List<Obj> objs;
	public static int idShip1, idShip2;
	public static int status;
	public static long time;
	public static final double minX = -500D, minY = -500D, maxX = 500D,
			maxY = 500D;
	public static PrintWriter pw;
	public static Process p1, p2;

	public static String getStatus() {
		int n = 0;
		for (int i = 0; i < objs.size(); i++)
			if (status == Constants.STATUS_START
					|| objs.get(i).getType() != Constants.TYPE_PLANET) {
				n++;
			}
		if (status == Constants.STATUS_START)
			n++;
		StringBuilder sb = new StringBuilder("");
		sb.append(time);
		sb.append(' ');
		sb.append(status);
		sb.append(' ');
		sb.append(n);
		if (status == Constants.STATUS_START)
			sb.append(' ').append(UUID.reserve()).append(' ')
					.append(Constants.TYPE_MAP).append(" 6 ").append(minX)
					.append(' ').append(minY).append(' ').append(maxX)
					.append(' ').append(maxY).append(' ')
					.append(Constants.R_SHIP).append(' ')
					.append(Constants.R_BULLET);
		for (int i = 0; i < objs.size(); i++)
			if (status == Constants.STATUS_START
					|| objs.get(i).getType() != Constants.TYPE_PLANET) {
				sb.append(' ');
				sb.append(objs.get(i).getData());
			}
		return sb.toString();
	}

	public static void init() {
		objs = new ArrayList<Obj>();
		objs.add(new Obj.ObjMoving.ObjShip(-1000, 0, 0, 0, Math.PI));
		idShip1 = objs.get(0).id;
		// System.out.println("id1: "+idShip1);
		objs.add(new Obj.ObjMoving.ObjShip(1000, 0, 0, 0, 0));
		idShip2 = objs.get(1).id;
		// System.out.println("id2: "+idShip2);
		objs.add(new Obj.ObjPlanet(0, 0, 10000));
		status = Constants.STATUS_START;
		try {
			pw = new PrintWriter("be.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pw.println(getStatus());
		status = Constants.STATUS_RUN;
	}
	
	public static String BenceGetConfiguration() {
		init();
		return getStatus();
	}

	public static String BenceUpdate(String s1, String s2) {
		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i).id == idShip1) {
				ObjShip ship = (ObjShip)objs.get(i);
				ship.dacc = Double.parseDouble(s1.split(" ")[0]);
				ship.ddeg = Double.parseDouble(s1.split(" ")[1]);
				ship.shot = Boolean.parseBoolean(s1.split(" ")[2]);
			} else if (objs.get(i).id == idShip2) {
				ObjShip ship = (ObjShip)objs.get(i);
				ship.dacc = Double.parseDouble(s2.split(" ")[0]);
				ship.ddeg = Double.parseDouble(s2.split(" ")[1]);
				ship.shot = Boolean.parseBoolean(s2.split(" ")[2]);
			}
			objs.get(i).update();
		}
		tick();
		return getStatus();
	}
	
	public static void BenceEnd() {
		end();
	}

	public static void tick() {
		time++;
		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i).id == idShip1 && p1 != null) {
				
			} else if (objs.get(i).id == idShip2 && p2 != null) {
				
			}
			objs.get(i).update();
		}
		for (int i = 0; i < objs.size(); i++)
			if (objs.get(i).shouldRemove) {
				if (objs.get(i).id == idShip1) {
					if (status == Constants.STATUS_WIN1)
						status = Constants.STATUS_DRAW;
					else
						status = Constants.STATUS_WIN2;
				} else if (objs.get(i).id == idShip2) {
					if (status == Constants.STATUS_WIN2)
						status = Constants.STATUS_DRAW;
					else
						status = Constants.STATUS_WIN1;
				}
				// System.out.println(time+" "+objs.get(i).id);
				objs.get(i).destroy();
				objs.remove(i--);
			}
		pw.println(getStatus());
	}

	public static void end() {
		// pw.println(getStatus());
		pw.close();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Starting simulation...");
		if(args.length == 2) {
			p1 = Runtime.getRuntime().exec(args[0]);
			p2 = Runtime.getRuntime().exec(args[1]);
		}
		init();
		while(status == Constants.STATUS_RUN) {
			tick();
		}
		if(p1 != null)
			p1.destroy();
		if(p2 != null)
			p2.destroy();
		end();
		System.out.println("Simulation done!");
	}
}
