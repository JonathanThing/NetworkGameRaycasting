import java.awt.image.BufferedImage;

abstract class Character extends Entity {

  private Weapon weapon;

  Character(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
      double speed, Weapon weapon) {
    super(position, width, height, name, angle, sprite, health, speed);
    this.weapon = weapon;

  }

  public Weapon getWeapon() {
    return this.weapon;
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

}