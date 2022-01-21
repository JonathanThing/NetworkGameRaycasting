import java.awt.image.BufferedImage;
import java.lang.Math;
import java.awt.Graphics;

class Player extends Character {
	private boolean slot1 = false;
	private boolean slot2 = false;
	private boolean slot3 = false;
	
	singleShot testgun = new singleShot(this.getPosition().getX(),this.getPosition().getY(),5,5,
			"test weapon", null, 20,20,30,200);
	
	singleShot pistol = new singleShot(this.getPosition().getX(),this.getPosition().getX(),5,5,"revolver",null,40,75,6,100);
	singleShot ar = new singleShot(this.getPosition().getX(),this.getPosition().getX(),5,5,"AR",null,10,10,30,300);
	singleShot sniper = new singleShot(this.getPosition().getX(),this.getPosition().getX(),5,5,"sniper",null,100,100,4,500);
	
	multiShot autoShotgun = new multiShot(this.getPosition().getX(),this.getPosition().getX(),5,5,"Auto Shotgun",null,20,20,8,500,3);
	multiShot Shotgun = new multiShot(this.getPosition().getX(),this.getPosition().getX(),5,5,"Shotgun",null,35,40,4,200,5);
	
    public void shoot(){ //BufferedImage sprite) {                
    	if(this.slot1 == true) {
    		pistol.shoot(getAngle(), getPosition(), getProjectilesList());
    	}
    	else if(this.slot2 == true) {
        	Shotgun.shoot(getAngle(), getPosition(),this.getProjectilesList());
    	}
    	else if(this.slot3 == true) {
    		ar.shoot(getAngle(), getPosition(),this.getProjectilesList());
    	}
    }  
    
    public void moveProjectiles() {
    	if(this.slot1 == true) {
    		pistol.moveProjectile(this.getProjectilesList());
    		//this.getProjectilesList().clear();
    	}
    	else if(this.slot2 == true) {
        	Shotgun.moveProjectile(this.getProjectilesList());
        	//this.getProjectilesList().clear();
    	}
    	else if(this.slot3 == true) {
    		ar.moveProjectile(this.getProjectilesList());
    		//this.getProjectilesList().clear();
    	}
    }
    
    public void drawPlayerProjectile(Graphics g, double offSetX, double offSetY) {
        for (int i = 0; i < getProjectilesList().size(); i++) { //loop through arrayList
            (getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
        }
    }

    public void movement(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight, Level map) {

        double xRaw = 0;
        double yRaw = 0;

        if (up) {
            yRaw += 1;
        }

        if (down) {
            yRaw -= 1;
        }

        if (right) {
            xRaw += 1;
        }

        if (left) {
            xRaw -= 1;
        }

        if (turnLeft) {
            this.getAngle().changeAngleValue(Math.toRadians(5));
        }

        if (turnRight) {
            this.getAngle().changeAngleValue(Math.toRadians(-5));
        }

        double forwardAngle = this.getAngle().getAngleValue();
        double sideAngle = Angle.checkLimit(forwardAngle - Math.PI / 2);

        Vector forwardVector = new Vector(Math.cos(forwardAngle) * yRaw, -Math.sin(forwardAngle) * yRaw);

        Vector sideVector = new Vector(Math.cos(sideAngle) * xRaw, -Math.sin(sideAngle) * xRaw);

        Vector movementVector = forwardVector.add(sideVector).normalized().multiplyByScalar(this.getSpeed());

        if (!movementVector.isZero()) {

            Vector futurePosition = this.getPosition().add(movementVector.multiplyByScalar(4));

            // door collision
            if (map.getMapTile((int) futurePosition.getY() / Const.BOXSIZE,
                               (int) futurePosition.getX() / Const.BOXSIZE) == 4) {
                map.setMapTile((int) futurePosition.getY() / Const.BOXSIZE, (int) futurePosition.getX() / Const.BOXSIZE,
                               0);
            }

            // wall side collision
            futurePosition = this.getPosition().add(movementVector.multiplyByScalar(2));
            if (map.getMapTile((int) this.getPosition().getY() / Const.BOXSIZE,
                               (int) futurePosition.getX() / Const.BOXSIZE) < 1) {
                this.moveLeft(movementVector.getX());
            }

            // wall forward collision
            if (map.getMapTile((int) futurePosition.getY() / Const.BOXSIZE,
                               (int) this.getPosition().getX() / Const.BOXSIZE) < 1) {
                this.moveDown(movementVector.getY());
            }
        }
    }

    public boolean getSlot1() {
    	return this.slot1;
    }
    public boolean setSlot2() {
    	return this.slot2;
    }
    public boolean getSlot3() {
    	return this.slot3;
    }
    
    public void setSlot1(boolean Slot) {
    	this.slot1 = Slot;
    }
    public void setSlot2(boolean Slot) {
    	this.slot2 = Slot;
    }
    public void setSlot3(boolean Slot) {
    	this.slot3 = Slot;
    }
    
    Player(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
           double speed,
           Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, weapon); // calls the constructor in the
        // character super
        // class
    }
}
