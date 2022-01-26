import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

abstract class Weapon{

	  private boolean shoot = false;
	  
	  private boolean reloading = false;
	  
	  private double damage;
	
	  private double fireRate;
	  
	  private int TotalAmmo;
	  
	  private int reloadTime;
	  
	  private long cooldown; 
	  
	  private long lastFire;
	  
	  private int Ammo = 0;
	  
	  TextureManager fireball;
	  
	  public boolean getShoot() {
		    return this.shoot;
		  }
	  
	  public double getFireRate() {
	    return this.fireRate;
	  }
	  
	  public boolean getReloading() {
		  return this.reloading;
	  }
	  
	  public double getDamage() {
		return this.damage;
	  }
	  
	  public int getTotalAmmo() {
		    return this.TotalAmmo;
	  }
	  
	  public int getAmmo() {
		    return this.Ammo;
	  }
	  
	  public int getReloadTime() {
		    return this.reloadTime;
	  }
	  
	  public long getCooldown() {
		    return this.cooldown;
	  }
	  
	  public long getLastFire() {
		  return this.lastFire;
	  }
	  
	  public void setShoot(boolean newShoot) {
		    this.shoot = newShoot;
		  }
	  
	  public void setReloading(boolean newReloading) {
		  this.reloading = newReloading;
	  }
	  
	  public void setAmmo(int Ammo) {
		    this.Ammo = Ammo;
	  }
	  
	  public void setCooldown(long newCooldown) {
		    this.cooldown = newCooldown;
	  }
	  
	  public void setLastFire(long newLastFire) {
		  this.lastFire = newLastFire;
	  }	  
	
	  abstract void shoot(Angle angle, Vector playerPos, long timer);
	  
	  //abstract void moveProjectile(ArrayList<Projectile> projectilesList);
	  
	  Weapon(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,
	      int Ammo, int reloadTime) {
	    this.damage = damage;
	    this.fireRate = fireRate;
	    this.TotalAmmo = Ammo;
	    this.reloadTime = reloadTime;
	    try {
	    	fireball = new TextureManager(ImageIO.read(new File("D://FireBallAnimation.png")));
	    }
	    catch(IOException e1){
	    	System.out.print("failed to load bullet image");
	    }
	  }
  
}
