import java.awt.image.BufferedImage;
import java.lang.Math;

class Player extends Character {

	private int ammo;

	public void movement(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight, Level map) {
		
		double xRaw = 0;
		double yRaw = 0;
		double xForward = 0;
		double yForward = 0;
		double xSide = 0;
		double ySide = 0;
		
		if (up) {
			yRaw += 1;
		}

		if (down) {
			yRaw -= 1;
		}

		if (right) {
			xRaw += 1;
		}

		if (left) {
			xRaw -= 1;
		}

		if (turnLeft) {
			this.getAngle().changeAngleValue(Math.toRadians(5));
		}

		if (turnRight) {
			this.getAngle().changeAngleValue(Math.toRadians(-5));
		}

		double forwardAngle = this.getAngle().getAngleValue();
		double sideAngle = Angle.checkLimit(forwardAngle - Math.PI / 2);
		
		xForward = Math.cos(forwardAngle) * yRaw;
		yForward = Math.sin(forwardAngle) * yRaw;

		ySide = Math.sin(sideAngle) * xRaw;
		xSide = Math.cos(sideAngle) * xRaw;

		Vector movementVector = new Vector(xForward + xSide, -yForward - ySide).normalized().multiplyByScalar(this.getSpeed());
		
		if (!movementVector.isZero()) {
					
			Vector futurePosition = this.getPosition().add(movementVector.multiplyByScalar(4));

			//door collision 
			if (map.getMapTile((int)futurePosition.getY() / Const.BOXSIZE, (int)futurePosition.getX() / Const.BOXSIZE) == 4) {
				map.setMapTile((int)futurePosition.getY() / Const.BOXSIZE, (int)futurePosition.getX() / Const.BOXSIZE, 0);
			}
			
			//wall side collision
			futurePosition = this.getPosition().add(movementVector.multiplyByScalar(2));
			if (map.getMapTile((int) this.getPosition().getY() / Const.BOXSIZE,(int) futurePosition.getX() / Const.BOXSIZE) < 1) {
				this.moveLeft(movementVector.getX());
			}
				
			//wall forward collision
			if (map.getMapTile((int) futurePosition.getY() / Const.BOXSIZE,(int) this.getPosition().getX()/ Const.BOXSIZE) < 1) {
				this.moveDown(movementVector.getY());
			}
		}
	}

	Player(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed,
			Weapon weapon) {
		super(position, width, height, name,angle, sprite, health, speed, weapon); // calls the constructor in the character super
																			// class
	}

}