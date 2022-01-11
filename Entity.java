import java.awt.image.BufferedImage;

abstract class Entity extends GameObject implements Moveable {

 private double health;
 private double speed;

 public double getHealth() {
  return this.health;
 }

 public void setHealth(double health) {
  this.health = health;
 }

 public double getSpeed() {
  return this.speed;
 }

 public void setSpeed(double speed) {
  this.speed = speed;
 }

 public void moveLeft(double n) {
  this.getPosition().changeX(n);
 }

 public void moveRight(double n) {
  this.getPosition().changeX(-n);
 }

 public void moveUp(double n) {
  this.getPosition().changeY(-n);
 }

 public void moveDown(double n) {
  this.getPosition().changeY(n);
 }

 Entity(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
   double speed) {
  super(position, width, height, name, angle, sprite);
  this.health = health;
  this.speed = speed;
 }

}