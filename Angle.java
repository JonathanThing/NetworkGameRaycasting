public class Angle {
	private double angle;
	public Angle (double angle) {
		this.angle = checkLimit(angle);
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public void changeAngle(double change) {
		this.angle = checkLimit(angle+change);
	}
	
	public void setAngle(double angle) {
		this.angle = checkLimit(angle);
	}
	
	//Limits the angle to between 0 and 2 pi
	public static double checkLimit(double angle) {
		if (angle > 2*Math.PI) {
			return angle % (2*Math.PI);
		} else if (angle < 0) {
			return angle + (2*Math.PI);
		} else {
			return angle;
		}
	}
}
