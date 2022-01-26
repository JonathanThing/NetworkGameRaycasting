import java.awt.image.BufferedImage;

public class singleShot extends Weapon{
	
	void shoot(Angle angle, Vector playerPos, long timer) {	
		
        this.setCooldown((timer - this.getLastFire()) / 100);
        
        if(this.getAmmo() == this.getTotalAmmo()) {
        	this.setAmmo(0);
        	this.setReloading(true);

        }
        else {
        	if(this.getReloading() == false) {
        		if(this.getFireRate() > this.getCooldown()) {  
        			 double yComponent =  Math.sin(angle.getValue());
        			 double xComponent =  Math.cos(angle.getValue());
        			 Game.addProjectileEntity(new Projectile(playerPos, 10, 10, "Bullet", angle, fireball, 0, 1, 0, 0.25, xComponent, yComponent, "player", 10));
        			 this.setAmmo(this.getAmmo()+1);	 
                }
                else {
                	setLastFire(System.currentTimeMillis());
                	this.setCooldown(0);    
                }  		    		
        	}
       }
    	if (this.getReloadTime() < this.getCooldown() && this.getAmmo() == 0) {
    		this.setReloading(false);
    	}
	}
	

	singleShot(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,int clipSize, int reloadTime) {
		super(x, y, width, height, name, sprite, damage, fireRate, clipSize, reloadTime);
		//lastFire = System.currentTimeMillis();
	}
}

 
