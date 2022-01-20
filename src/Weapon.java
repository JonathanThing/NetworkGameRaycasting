import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Weapon{

  private int size;

  private double damage;

  private double fireRate;
  
  private int AmmoSize;
  
  private long lastFire;
  
  private int reloadTime;
  
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
	  return this.AmmoSize;
  }
  
  public int getReloadTime() {
	  return this.reloadTime;
  }

  long setCooldown(long lastFire, long timer) {
		return(timer - lastFire) / 10;
	}
  
  abstract void shoot(Angle angle, Vector playerPos, ArrayList<Projectile> projectilesList);
  
  abstract void moveProjectile(ArrayList<Projectile> projectilesList);
  
  Weapon(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,
      int size, int AmmoSize, int reloadTime) {
    this.damage = damage;
    this.fireRate = fireRate;
    this.size = size;
    this.AmmoSize = AmmoSize;
    this.reloadTime = reloadTime;
  }


  
}
