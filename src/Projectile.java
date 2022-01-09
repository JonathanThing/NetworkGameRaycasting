import java.awt.image.BufferedImage;

import java.awt.Graphics;

class Projectile extends Entity{

  private double changeX;
  private double changeY; 
  public double getChangeX(){
    return this.changeX;
  }
  
  public double getChangeY(){
    return this.changeY;
  }

  public void draw(Graphics g, double offSetX, double offSetY) {
    g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
  }

  Projectile(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite, int health, double speed, double changeX, double changeY){
    super(x, y, width, height, name, angle, sprite, health, speed); 
    this.changeX = changeX;
    this.changeY = changeY;
  }
  
}