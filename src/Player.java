import java.lang.Math;

class Player extends Character {
    private int ammo;
    private boolean slot1 = false;
    private boolean slot2 = false;
    private boolean slot3 = false;
    
    SingleShot pistol = new SingleShot(this.getPosition().getX(), this.getPosition().getY(),5,5, "Pistol", null, 40, 5, 6, 25);
    SingleShot ar = new SingleShot(this.getPosition().getX(), this.getPosition().getY(),5,5, "ar", null, 20, 1, 25, 40);
    SingleShot rifle = new SingleShot(this.getPosition().getX(), this.getPosition().getY(),5,5, "rifle", null, 200, 15, 2, 75);
    //dmg, firerate, bullets, reloadtime
    
    public void shoot(long timer) {    	
    	if(this.slot1 == true) {
    		pistol.shoot(getAngle(), this.getPosition().clone(), timer);
    	}
    	else if(this.slot2 == true) {
    		ar.shoot(getAngle(), this.getPosition().clone(), timer);
    	}
    	else if(this.slot3 == true) {
    		rifle.shoot(getAngle(), this.getPosition().clone(), timer);
    	}
    		
      }

    public synchronized void movement(boolean up, boolean down, boolean left, boolean right, boolean turnLeft,
            boolean turnRight, Level map, int deltaX) {

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
            this.getAngle().changeValue(Math.toRadians(5));
        }

        if (turnRight) {
            this.getAngle().changeValue(Math.toRadians(-5));
        }

        double forwardAngle = this.getAngle().getValue();
        double sideAngle = Angle.checkLimit(forwardAngle - Math.PI / 2);

        Vector forwardVector = new Vector(Math.cos(forwardAngle) * yRaw, -Math.sin(forwardAngle) * yRaw);

        Vector sideVector = new Vector(Math.cos(sideAngle) * xRaw, -Math.sin(sideAngle) * xRaw);

        Vector movementVector = forwardVector.add(sideVector).normalized().multiplyByScalar(this.getSpeed());

        if (!movementVector.isZero()) {

            Vector futurePosition = this.getPosition().add(movementVector.multiplyByScalar(4));

            // door collision
            if (map.getMapTile((int) futurePosition.getY() / Const.BOX_SIZE,
                    (int) futurePosition.getX() / Const.BOX_SIZE) == 4) {
                map.setMapTile((int) futurePosition.getY() / Const.BOX_SIZE,
                        (int) futurePosition.getX() / Const.BOX_SIZE,
                        0);
            }

            // wall side collision
            futurePosition = this.getPosition().add(movementVector.multiplyByScalar(2));
            if (map.getMapTile((int) this.getPosition().getY() / Const.BOX_SIZE,
                    (int) futurePosition.getX() / Const.BOX_SIZE) < 1) {
                this.moveLeft(movementVector.getX());
            }

            // wall forward collision
            if (map.getMapTile((int) futurePosition.getY() / Const.BOX_SIZE,
                    (int) this.getPosition().getX() / Const.BOX_SIZE) < 1) {
                this.moveDown(movementVector.getY());
            }
        }
    }

    public void run() {
    }

    Player(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, double health,
            double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon); 
        ammo = 40;
    }

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
	public void changeAmmo(int change) {
		this.ammo += change;
	}
	
	public void setSlot1(boolean slot) {
		this.slot1 = slot;
	}
	
	public void setSlot2(boolean slot) {
		this.slot2 = slot;
	}
	
	public void setSlot3(boolean slot) {
		this.slot3 = slot;
	}
	
	
}
