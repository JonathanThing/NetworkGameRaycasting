import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Character extends Entity {

  private Weapon weapon;
  private ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();

  Character(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
  double speed, double spriteZOffset, double spriteScale,  Weapon weapon) {
    super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale);
    this.weapon = weapon;

  }
  
  public ArrayList<Projectile> getProjectilesList() {
      return projectilesList;
  }
  
  public Weapon getWeapon() {
    return this.weapon;
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

}