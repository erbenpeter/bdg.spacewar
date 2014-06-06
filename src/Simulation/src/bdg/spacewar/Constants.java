package bdg.spacewar;

public class Constants {
	public static final int STATUS_START = 0;
	public static final int STATUS_RUN = 1;
	public static final int STATUS_DRAW = 2;
	public static final int STATUS_WIN1 = 3;
	public static final int STATUS_WIN2 = 4;
	public static final int STATUS_ERROR = 5;

	public static final int TYPE_SHIP = 0;
	public static final int TYPE_BULLET = 1;
	public static final int TYPE_EXPLOSION = 2;
	public static final int TYPE_PLANET = 3;
	public static final int TYPE_MAP = 4;

	public static final double R_SHIP = 50D;
	public static final double R_BULLET = 1D;
	public static final double R_PLANET = 100D;
	
	public static final double A_SHIP = 0.5D;
	public static final double W_SHIP = Math.PI/16;
	public static final double V_BULLET = 10D;
	public static final int T_BULLET = 10;
}