
import java.awt.image.BufferedImage; 

abstract class Entity extends GameObject implements Moveable{
  
  private double health;
  private double speed;
  

  public double getHealth(){
    return this.health;
  }
  
  public void setHealth(double health){
    this.health = health;
  }
  
  public double getSpeed(){
    return this.speed;
  }
  
  public void setSpeed(double speed){
    this.speed = speed;
  }
  
  public void moveLeft(double n) {
    this.setX(this.getX()-n);
  }
  
  public void moveRight(double n) {
    this.setX(this.getX()+n);
  }
  
  public void moveUp(double n) {
    this.setY(this.getY()-n);
  }
  
  public void moveDown(double n) {
    this.setY(this.getY()+n);
  }
  
  Entity(double x, double y, int width, int height, String name, BufferedImage sprite, double health, double speed){
    super(x, y, width,  height, name, sprite);
    this.health = health;
    this.speed = speed;
  }
  
}