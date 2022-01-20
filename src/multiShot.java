import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class multiShot extends Weapon{
	private long lastFire;
	private long timer;
	private long cooldown;
	private boolean reloading = false;
	private int shots;
	
	
	
	void shoot(Angle angle, Vector playerPos, ArrayList<Projectile> projectilesList) {
        cooldown = setCooldown(this.lastFire, this.timer);
        if(projectilesList.size() == this.getAmmoSize()) {
        	projectilesList.clear();
        	reloading = true;
        }
        else {
        	if(reloading == false) {
        		if(this.cooldown < 5) {    	
            		for(int i = 0; i < shots;i++) {
                		double yComponent = 0;
                		double xComponent = 0;
            			if(i%2 == 1) {
                    		yComponent =  -1*Math.sin((angle.getAngleValue()-0.1*i));
                    		xComponent =  -1*Math.cos((angle.getAngleValue()-0.1*i));	
            			}
            			else if(i%2 ==0) {
                    		yComponent =  -1*Math.sin((angle.getAngleValue()-0.1*i));
                    		xComponent =  -1*Math.cos((angle.getAngleValue()-0.1*i));		            				
            			}

            			projectilesList.add(new Projectile(new Vector(playerPos.getX(),playerPos.getY()), 20, 20, "Bullet", angle, null, 10, 10, xComponent, yComponent));
            		}
                }
                else if(this.cooldown > this.getFireRate()) {
                	lastFire = System.currentTimeMillis();
            	    cooldown = 0;           
                }  		    		
        	}
        	if (this.getReloadTime() < cooldown) {
        		reloading = false;
        	}
        }
	}
	
	void moveProjectile(ArrayList<Projectile> projectilesList) {
	    this.timer = System.currentTimeMillis();
		for(int i = 0; i < projectilesList.size(); i++) {
				(projectilesList.get(i)).moveUp(projectilesList.get(i).getChangeY()*-projectilesList.get(i).getSpeed()); //moves the projectils on the y-axis
		        (projectilesList.get(i)).moveLeft(projectilesList.get(i).getChangeX()*-projectilesList.get(i).getSpeed()); //moves the projectils on the x-axis   
		}
	    if(projectilesList.size() > this.getAmmoSize()) {
	    	projectilesList.remove(0);
	    }
	}

	multiShot(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,
			int size, int clipSize, int reloadTime, int numOfShots) {
		super(x, y, width, height, name, sprite, damage, fireRate, size, clipSize, reloadTime);
		this.shots = numOfShots;
	}
	
	
	
	
}
