package bdg.spacewar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import bdg.spacewar.Obj.ObjMoving.*;

public class SpaceWar {
	static class Player {
		int shipID, id;
		long lastCommTime, lastShotTime;
		Process p;
		InputStream in;
		OutputStream out;

		public Player(String str, int shipID, int id) {
			try {
				this.p = Runtime.getRuntime().exec(str);
				this.in = p.getInputStream();
				this.out = p.getOutputStream();
			} catch (IOException e) {
				System.err.println("Error loading player!");
				status = Constants.STATUS_ERROR;
			}
		}

		public String getReply() throws IOException {
			String msg = "";
			char c = ' ';
			while (c != '\n') {
				if (System.currentTimeMillis() - this.lastCommTime > maxWaitTime) {
					System.err.println("Player " + this.id + " timed out!");
					status = Constants.STATUS_ERROR;
					return null;
				}
				if (this.in.available() > 0) {
					c = (char) this.in.read();
					msg += c;
				}
			}
			return msg.substring(0, msg.length() - 1);
		}

		public void destroy() {
			try {
				this.in.close();
				this.out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.p.destroy();
		}
	}

	static List<Obj> objs;
	static int status;
	static long time;
	static PrintWriter pw;
	static Player[] players;
	static int playerCount;

	// Temporal values
	static final long maxWaitTime = 500;
	static final double minX = -1000D, minY = -1000D, maxX = 1000D,
			maxY = 1000D;

	static String getStatus() {
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
					.append(Constants.TYPE_MAP).append(" 10 ").append(minX)
					.append(' ').append(minY).append(' ').append(maxX)
					.append(' ').append(maxY).append(' ')
					.append(Constants.R_SHIP).append(' ')
					.append(Constants.A_SHIP).append(' ')
					.append(Constants.W_SHIP).append(' ')
					.append(Constants.R_BULLET).append(' ')
					.append(Constants.V_BULLET).append(' ')
					.append(Constants.T_BULLET);
		for (int i = 0; i < objs.size(); i++)
			if (status == Constants.STATUS_START
					|| objs.get(i).getType() != Constants.TYPE_PLANET) {
				sb.append(' ');
				sb.append(objs.get(i).getData());
			}
		return sb.toString();
	}

	static void writeData() {
		String str = new StringBuilder(getStatus()).append('\n').toString();
		if (players != null)
			for (int i = 0; i < players.length; i++)
				try {
					players[i].out.flush();
					players[i].out.write(str.getBytes());
					players[i].out.flush();
					players[i].lastCommTime = System.currentTimeMillis();
				} catch (IOException e) {
					System.err.println("Couldn't send data to program 1!");
					status = Constants.STATUS_ERROR;
				}
		pw.print(str);
	}

	static void init(String[] args) {
		objs = new ArrayList<Obj>();
		if (args != null && args.length > 0) {
			players = new Player[args.length];
			playerCount = args.length;
			for (int i = 0; i < args.length; i++) {
				double deg = Math.PI * 2D * i / args.length;
				ObjShip ship = new ObjShip(Math.cos(deg) * Constants.R_PLANET
						* 5, Math.sin(deg) * Constants.R_PLANET * 5, 0D, 0D,
						deg);
				ship.player = i;
				objs.add(ship);
				players[i] = new Player(args[i], ship.id, i);
			}
		} else if (playerCount > 0)
			for (int i = 0; i < playerCount; i++) {
				double deg = Math.PI * 2D * i / playerCount;
				ObjShip ship = new ObjShip(Math.cos(deg) * Constants.R_PLANET
						* 5, Math.sin(deg) * Constants.R_PLANET * 5, 0D, 0D,
						deg);
				ship.player = i;
				objs.add(ship);
			}
		objs.add(new Obj.ObjPlanet(0, 0, 10000));
		status = Constants.STATUS_START;
		try {
			pw = new PrintWriter("be.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writeData();
		status = Constants.STATUS_RUN;
	}

	static void tick() {
		System.out.println(time++);
		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i) instanceof ObjShip && players != null) {
				ObjShip ship = (ObjShip) objs.get(i);
				String msg = null;
				try {
					msg = players[ship.player].getReply();
				} catch (IOException e) {
					System.err.println("Error retrieving reply from player "
							+ ship.player + "!");
					status = Constants.STATUS_ERROR;
				}
				if (msg != null) {
					ship.ddeg = Double.parseDouble(msg.split(" ")[0])
							* Constants.W_SHIP;
					ship.dacc = Double.parseDouble(msg.split(" ")[1])
							* Constants.A_SHIP;
					ship.shot = Boolean.parseBoolean(msg.split(" ")[2]);
					if (ship.ddeg < -1 || ship.ddeg > 1 || ship.dacc < -1
							|| ship.dacc > 1) {
						System.err.println("Invalid input form player "
								+ ship.player + "!");
						status = Constants.STATUS_ERROR;
					}
				}
			}
			objs.get(i).update();
		}
		for (int i = 0; i < objs.size(); i++)
			if (objs.get(i).shouldRemove) {
				if (objs.get(i) instanceof ObjShip) {
					ObjShip ship = (ObjShip) objs.get(i);
					if (players != null) {
						players[ship.player].destroy();
						players[ship.player] = null;
					}
					playerCount--;
					if (playerCount <= 1)
						status = Constants.STATUS_DRAW;
				}
				objs.get(i).destroy();
				objs.remove(i--);
			}
		writeData();
	}

	static void end() {
		if (players != null)
			for (int i = 0; i < players.length; i++)
				if (players[i] != null)
					players[i].destroy();
		pw.close();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Starting simulation...");
		args = new String[] { "proba", "prg2" };
		init(args);
		while (status == Constants.STATUS_RUN) {
			tick();
		}
		end();
		System.out.println("Simulation done!");
	}

	static String BenceGetConfiguration() {
		init(null);
		return getStatus();
	}

	static String BenceUpdate(String[] input) {
		int x = 0;
		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i) instanceof ObjShip) {
				ObjShip ship = (ObjShip) objs.get(i);
				ship.ddeg = Double.parseDouble(input[x].split(" ")[0])
						* Constants.W_SHIP;
				ship.dacc = Double.parseDouble(input[x].split(" ")[1])
						* Constants.A_SHIP;
				ship.shot = Boolean.parseBoolean(input[x].split(" ")[2]);
				if (ship.ddeg < -1 || ship.ddeg > 1 || ship.dacc < -1
						|| ship.dacc > 1) {
					System.err.println("Invalid input form player "
							+ ship.player + "!");
					status = Constants.STATUS_ERROR;
				}
				x++;
			}
			objs.get(i).update();
		}
		tick();
		return getStatus();
	}

	static void BenceEnd() {
		end();
	}
}
