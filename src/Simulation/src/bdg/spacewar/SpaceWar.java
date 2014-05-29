package bdg.spacewar;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

public class SpaceWar extends Thread {
	public List<Obj> objs;
	public int idShip1, idShip2;
	public int status;
	public long time;
	public PrintWriter pw;
	public Process p1, p2;

	public String getStatus() {
		int n = 0;
		for (int i = 0; i < this.objs.size(); i++)
			if (this.status == Constants.STATUS_START
					|| this.objs.get(i).getType() != Constants.TYPE_PLANET) {
				n++;
			}
		if (this.status == Constants.STATUS_START)
			n++;
		StringBuilder sb = new StringBuilder("");
		sb.append(this.time);
		sb.append(' ');
		sb.append(this.status);
		sb.append(' ');
		sb.append(n);
		if (this.status == Constants.STATUS_START)
			sb.append(' ').append(Constants.TYPE_MAP).append(' ')
					.append(UUID.reserve()).append(" 6 ")
					.append("-3000 -3000 3000 3000 ").append(Constants.R_SHIP)
					.append(' ').append(Constants.R_BULLET);
		for (int i = 0; i < this.objs.size(); i++)
			if (this.status == Constants.STATUS_START
					|| this.objs.get(i).getType() != Constants.TYPE_PLANET) {
				sb.append(' ');
				sb.append(this.objs.get(i).getData());
			}
		return sb.toString();
	}

	public SpaceWar(String[] args) {

	}

	public void init() {
		this.objs = new ArrayList<Obj>();
		this.objs.add(new Obj.ObjMoving.ObjShip(-1000, 0, 0, 0, Math.PI));
		this.idShip1 = this.objs.get(0).id;
		//System.out.println("id1: "+this.idShip1);
		this.objs.add(new Obj.ObjMoving.ObjShip(1000, 0, 0, 0, 0));
		this.idShip2 = this.objs.get(1).id;
		//System.out.println("id2: "+this.idShip2);
		this.objs.add(new Obj.ObjPlanet(0, 0, 10000));
		this.status = Constants.STATUS_START;
		try {
			this.pw = new PrintWriter("be.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.pw.println(this.getStatus());
		this.status = Constants.STATUS_RUN;
	}

	public void tick() {
		for (int i = 0; i < this.objs.size(); i++) {
			if (this.objs.get(i).id == this.idShip1) {
				// TODO
			} else if (this.objs.get(i).id == this.idShip2) {
				// TODO
			}
			this.objs.get(i).update(this.objs);
		}
		for (int i = 0; i < this.objs.size(); i++)
			if (this.objs.get(i).shouldRemove) {
				if (this.objs.get(i).id == this.idShip1) {
					if (this.status == Constants.STATUS_WIN1)
						this.status = Constants.STATUS_DRAW;
					else
						this.status = Constants.STATUS_WIN2;
				} else if (this.objs.get(i).id == this.idShip2) {
					if (this.status == Constants.STATUS_WIN2)
						this.status = Constants.STATUS_DRAW;
					else
						this.status = Constants.STATUS_WIN1;
				}
				//System.out.println(this.time+" "+this.objs.get(i).id);
				this.objs.get(i).destroy();
				this.objs.remove(i--);
			}
	}

	public void end() {
		//this.pw.println(this.getStatus());
		this.pw.close();
	}

	@Override
	public void run() {
		System.out.println("Starting simulation...");
		this.init();
		for (this.time = 1; this.status == Constants.STATUS_RUN; this.time++) {
			tick();
			this.pw.println(this.getStatus());
		}
		this.end();
		System.out.println("Simulation done!");
	}

	public static void main(String[] args) {
		new SpaceWar(args).start();
	}
}
