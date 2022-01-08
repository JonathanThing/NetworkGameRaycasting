import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;

abstract class Enemy extends Character {
  
  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.setColor(Color.RED);
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);
  }  

  Enemy(double x, double y, int width, int height, String name, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(x, y, width, height, name, sprite, health, speed, weapon);
  }
  
}