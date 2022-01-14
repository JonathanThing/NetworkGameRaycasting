import java.awt.image.BufferedImage;

class Projectile extends Entity {

  private double changeX;
  private double changeY;

  public double getChangeX() {
    return this.changeX;
  }

  public double getChangeY() {
    return this.changeY;
  }

  Projectile(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, int health,
      double speed, double changeX, double changeY) {
    super(position, width, height, name, angle, sprite, health, speed);
    this.changeX = changeX;
    this.changeY = changeY;
  }

}