import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

abstract class Weapon{

  private double damage;

  private double fireRate;
  
  private int AmmoSize;
  
  private long lastFire;
  
  private int reloadTime;
  
  TextureManager fireball;
  
  public double getFireRate() {
    return this.fireRate;
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
  
  Weapon(double x, double y, int width, int height, String name, TextureManager sprite, double damage, double fireRate,
      int AmmoSize, int reloadTime) {
    this.damage = damage;
    this.fireRate = fireRate;
    this.AmmoSize = AmmoSize;
    this.reloadTime = reloadTime;
    try {
		fireball = new TextureManager(ImageIO.read(new File("C://FireBallAnimation.png")));
	} catch (IOException e1) {
		System.out.println("failed to get image");
		e1.printStackTrace();
	}
  }


  
}
