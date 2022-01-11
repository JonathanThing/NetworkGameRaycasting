public class Vector {
 private double x;
 private double y;
 
 public Vector(double x, double y) {
  this.x = x;
  this.y = y;
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
 
 public boolean isZero() {
  if (this.x == 0 && this.y == 0) {
   return true;
  } else {
   return false;
  }
 }
 
}