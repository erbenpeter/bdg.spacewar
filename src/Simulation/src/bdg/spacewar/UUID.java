package bdg.spacewar;

public class UUID {
	private static final int MAXID = 65536;
	private static boolean[] used = new boolean[MAXID];
	private static int cid = 0;

	/**
	 * Visszaad egy m�g szabad id-t.
	 */
	public static int reserve() {
		while (used[cid]) {
			cid++;
			if (cid >= MAXID)
				cid = 0;
		}
		used[cid] = true;
		return cid;
	}

	/**
	 * Felszabad�tja a megadott id-t.
	 */
	public static void remove(int id) {
		used[id] = false;
	}
}
