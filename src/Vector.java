public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector clone() {
		return new Vector(this.getX(),this.getY());
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void changeX(double change) {
		this.x += change;
	}
	
	public void changeY(double change) {
		this.y += change;
	}
	
	public Vector normalized() {
		double hypotenuse = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return new Vector(x/hypotenuse , y/hypotenuse);
	}
	
	public Vector add(Vector vector2) {
		return new Vector(this.x + vector2.getX(), this.y + vector2.getY());
	}
	
	public Vector subtract(Vector vector2) {
		return new Vector(this.x - vector2.getX(), this.y - vector2.getY());
	}
	
	public Vector multiplyByScalar(double value) {
		return new Vector(this.x * value, this.y * value);
	}
	
	public Vector divideByScalar(double value) {
		return new Vector(this.x / value, this.y / value);
	}
	
	public Vector rotateVector(double radians) {
		return new Vector(this.x*Math.cos(radians) - this.y*Math.sin(radians), this.x*Math.sin(radians) + this.y*Math.cos(radians));
	}
	
	public double distance(Vector other) {
		return Math.sqrt(Math.pow(this.getX()-other.getX(), 2) + Math.pow(this.getY()-other.getY(), 2));
	}
	
	public double magnitude() {
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
	}
	
	public boolean inRange(Level map) {
		if (this.getX() < map.getColumns()*Const.BOX_SIZE && this.getX() >= 0 && this.getY() < map.getRows()*Const.BOX_SIZE && this.getY() >= 0) {
			return true;
		} 
		
		return false;
	}
	
	public Vector flipXY() {
		return new Vector(this.y,this.x);
	}
	
	public boolean isZero() {
		if (this.x == 0 && this.y == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
	
}
