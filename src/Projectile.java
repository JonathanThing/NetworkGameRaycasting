import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Projectile extends Entity {

  private double changeX;
  private double changeY;

  public double getChangeX() {
    return this.changeX;
  }

  public double getChangeY() {
    return this.changeY;
  }
  
  public void draw(Graphics g, double offSetX, double offSetY) {
      //System.out.println("drawing projectile");
      g.fillRect((int) (getPosition().getX() - getWidth() / 2 + offSetX), (int) (getPosition().getY() - getHeight() / 2 + offSetY), getWidth(), getHeight()); //draw the projectile
  }

  Projectile(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health, double speed, double spriteZOffset, double spriteScale, double changeX, double changeY) {
    super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale);
    this.changeX = changeX;
    this.changeY = changeY;
  }

}