package bdg.spacewar;

public class Obj {
	/** Az objektum egyéni azonosítója. */
	public int id;
	public boolean shouldRemove;

	public Obj() {
		this.id = UUID.reserve();
		this.shouldRemove = false;
	}

	public int getType() {
		return -1;
	}

	public boolean hasCoords() {
		return false;
	}

	public double getX() {
		return 0D;
	}

	public double getY() {
		return 0D;
	}

	public double getRadius() {
		return 0D;
	}

	public boolean hasMass() {
		return false;
	}

	public double getMass() {
		return 0D;
	}

	/**
	 * Frissíti az objektumot.
	 */
	public void update() {

	}
	
	public void postUpdate() {
		
	}

	/**
	 * Megadja az objektum leírását.
	 */
	public String getData() {
		return this.id + " " + this.getType();
	}

	/**
	 * Felszabadítja az objektum id-jét.
	 */
	public void destroy() {
		UUID.remove(this.id);
	}

	public static class ObjMoving extends Obj {
		public double x, y, vx, vy, nx, ny;

		public ObjMoving(double x, double y, double vx, double vy) {
			super();
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.nx = x + vx;
			this.ny = y + vy;
		}

		@Override
		public boolean hasCoords() {
			return true;
		}

		@Override
		public double getX() {
			return this.x;
		}

		@Override
		public double getY() {
			return this.y;
		}

		@Override
		public void update() {
			double gx = 0D, gy = 0D;
			for (int i = 0; i < SpaceWar.objs.size(); i++)
				if (SpaceWar.objs.get(i).id != this.id
						&& SpaceWar.objs.get(i).hasCoords()) {
					if (SpaceWar.objs.get(i).hasMass()) {
						double r2 = (SpaceWar.objs.get(i).getX() - this.x)
								* (SpaceWar.objs.get(i).getX() - this.x)
								+ (SpaceWar.objs.get(i).getY() - this.y)
								* (SpaceWar.objs.get(i).getY() - this.y);
						double ratio = SpaceWar.objs.get(i).getMass() / r2
								/ Math.sqrt(r2);
						gx += (SpaceWar.objs.get(i).getX() - this.x) * ratio;
						gy += (SpaceWar.objs.get(i).getY() - this.y) * ratio;
					}
					if (SpaceWar.objs.get(i).getRadius()
							* SpaceWar.objs.get(i).getRadius()
							+ this.getRadius() * this.getRadius() >= (SpaceWar.objs
							.get(i).getX() - this.x)
							* (SpaceWar.objs.get(i).getX() - this.x)
							+ (SpaceWar.objs.get(i).getY() - this.y)
							* (SpaceWar.objs.get(i).getY() - this.y))
						this.shouldRemove = true;
				}
			this.vx += gx;
			this.vy += gy;
			this.nx = this.x + this.vx;
			this.ny = this.y + this.vy;
		}
		
		@Override
		public void postUpdate(){
			this.x = this.nx;
			this.y = this.ny;
		}

		public static class ObjShip extends ObjMoving {
			public double deg;
			public double ddeg, dacc;
			public boolean shot;
			public int player;
			public long lastShotTime;

			public ObjShip(double x, double y, double vx, double vy, double deg) {
				super(x, y, vx, vy);
				this.deg = deg;
				this.lastShotTime = -Constants.T_BULLET;
			}

			@Override
			public int getType() {
				return Constants.TYPE_SHIP;
			}

			@Override
			public double getRadius() {
				return Constants.R_SHIP;
			}

			@Override
			public void update() {
				this.deg += this.ddeg;
				while (this.deg < 0D)
					this.deg += 2 * Math.PI;
				while (this.deg >= 2 * Math.PI)
					this.deg -= 2 * Math.PI;
				this.vx += Math.cos(this.deg) * this.dacc;
				this.vy += Math.sin(this.deg) * this.dacc;
				super.update();
				if (this.shot
						&& SpaceWar.time - this.lastShotTime > Constants.T_BULLET) {
					this.lastShotTime = SpaceWar.time;
					SpaceWar.objs.add(new Obj.ObjMoving.ObjBullet(this.x
							+ Math.cos(this.deg)
							* (Constants.R_BULLET + Constants.R_SHIP) * 1.1, this.y
							+ Math.sin(this.deg)
							* (Constants.R_BULLET + Constants.R_SHIP) * 1.1, this.vx
							+ Math.cos(this.deg) * Constants.V_BULLET, this.vy
							+ Math.sin(this.deg) * Constants.V_BULLET));
				}
				if (this.x < SpaceWar.minX || this.x > SpaceWar.maxX
						|| this.y < SpaceWar.minY || this.y > SpaceWar.maxY)
					this.shouldRemove = true;
			}

			@Override
			public String getData() {
				return super.getData() + " 5 " + this.x + " " + this.y + " "
						+ this.vx + " " + this.vy + " " + this.deg;
			}
		}

		public static class ObjBullet extends ObjMoving {
			public ObjBullet(double x, double y, double vx, double vy) {
				super(x, y, vx, vy);
			}

			@Override
			public int getType() {
				return Constants.TYPE_BULLET;
			}

			@Override
			public double getRadius() {
				return Constants.R_BULLET;
			}

			@Override
			public String getData() {
				return super.getData() + " 4 " + this.x + " " + this.y + " "
						+ this.vx + " " + this.vy;
			}
		}
	}

	public static class ObjPlanet extends Obj {
		public double x, y, mass;

		public ObjPlanet(double x, double y, double m) {
			super();
			this.x = x;
			this.y = y;
			this.mass = m;
		}

		@Override
		public int getType() {
			return Constants.TYPE_PLANET;
		}

		@Override
		public boolean hasCoords() {
			return true;
		}

		@Override
		public double getX() {
			return this.x;
		}

		@Override
		public double getY() {
			return this.y;
		}

		public double getRadius() {
			return Constants.R_PLANET;
		}

		public boolean hasMass() {
			return true;
		}

		public double getMass() {
			return this.mass;
		}

		@Override
		public String getData() {
			return super.getData() + " 4 " + this.x + " " + this.y + " "
					+ Constants.R_PLANET + " " + this.mass;
		}
	}
}