import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Weapon {

  private int size;

  private double damage;

  private double fireRate;
  
  private long lastFire;
  
  private int clipSize;
  
  public double getFireRate() {
    return this.fireRate;
  }

  public int getSize() {
    return this.size;
  }
  
  public double getDamage() {
	return this.damage;
  }
  
  public long getLastFire() {
	  return this.lastFire;
  }
  
  public int getAmmoSize() {
	    return this.clipSize;
	  }

  Weapon(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,
      int size, int clipSize) {
    this.damage = damage;
    this.fireRate = fireRate;
    this.size = size;
    this.clipSize = clipSize;
  }
  
  abstract void shoot(Angle angle, Vector playerPos, ArrayList<Projectile> projectilesList);
  
  abstract void moveProjectile(ArrayList<Projectile> projectilesList);

}
