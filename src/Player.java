import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character {

	private int ammo;

	public void draw(Graphics g, double offSetX, double offSetY) {

		g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX),
				(int) (getY() - getHeight() / 2 - offSetY), null);
	}

	public void movement(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight, Level map) {
		
		double xRaw = 0;
		double yRaw = 0;
		double xForward = 0;
		double yForward = 0;
		double xSide = 0;
		double ySide = 0;
		double forward = 0;
		double side = 0;

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

		xForward = Math.cos(this.getAngle().getAngleValue()) * yRaw;
		yForward = Math.sin(this.getAngle().getAngleValue()) * yRaw;

		ySide = Math.sin(Angle.checkLimit(this.getAngle().getAngleValue() - Math.PI / 2)) * xRaw;
		xSide = Math.cos(Angle.checkLimit(this.getAngle().getAngleValue() - Math.PI / 2)) * xRaw;

		forward = yForward + ySide;
		side = xForward + xSide;

		double hyp = Math.sqrt(Math.pow(forward, 2) + Math.pow(side, 2));
		
		if (hyp != 0) {
			
			if (map.getMapTile((int) ((this.getY() - ((forward/hyp) * this.getSpeed() * 4))/Const.BOXSIZE),(int) ((this.getX() + ((side/hyp) * this.getSpeed() * 4))/Const.BOXSIZE)) == 4) {
				map.setMapTile((int) ((this.getY() - ((forward/hyp) * this.getSpeed() * 4))/Const.BOXSIZE),(int) ((this.getX() + ((side/hyp) * this.getSpeed() * 4))/Const.BOXSIZE), 0);
			}
			
			if (map.getMapTile((int) (this.getY()/Const.BOXSIZE),(int) ((this.getX() + ((side/hyp) * this.getSpeed()*2))/Const.BOXSIZE)) < 1) {
				this.moveRight((side / hyp) * this.getSpeed());
				
			}
				
			if (map.getMapTile((int) ((this.getY() - ((forward/hyp) * this.getSpeed()*2))/Const.BOXSIZE),(int) (this.getX()/Const.BOXSIZE)) < 1) {
				this.moveUp(((forward / hyp) * this.getSpeed()));
			}
		}
	}

	Player(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed,
			Weapon weapon) {
		super(x, y, width, height, name,angle, sprite, health, speed, weapon); // calls the constructor in the character super
																			// class
	}

}