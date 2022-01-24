import java.awt.image.BufferedImage;
import java.lang.Math;
import java.awt.Graphics;

class Player extends Character {

    private int ammo;

    public void shoot(TextureManager sprites){ //BufferedImage sprite) {
        double yComponent = -1* Math.sin(getAngle().getValue());
        double xComponent = -1* Math.cos(getAngle().getValue());

        this.getProjectilesList().add(new Projectile(this.getPosition().clone(), 10, 10, "Bullet", getAngle(), sprites, 0, 10, 0, 0.25, xComponent, yComponent));
        //Projectile(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, double spriteZOffset, double spriteScale, double changeX, double changeY) {

    }

    public void moveProjectile() {
    	
    	
        for (Projectile currentProjectile :  this.getProjectilesList()) { //loops through arrayList of projectiles

        	//for some reason it applies to all the projectiles, odd
        	long currentTime = System.currentTimeMillis();       	
        	if (currentProjectile.getSprites().getLastAnimationChange() + 250 <= currentTime) {
        		currentProjectile.getSprites().setLastAnimationChange(currentTime);
        		currentProjectile.getSprites().changeAnimationNumber(1);
        	}
        
        	
            (currentProjectile).moveDown(currentProjectile.getChangeY()); //moves the projectils on the y-axis
            (currentProjectile).moveRight(currentProjectile.getChangeX()); //moves the projectils on the x-axis

        }

    }

    public void movement(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight,
                         Level map) {

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
                map.setMapTile((int) futurePosition.getY() / Const.BOX_SIZE, (int) futurePosition.getX() / Const.BOX_SIZE,
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

    public void drawPlayerProjectile(Graphics g, double offSetX, double offSetY) {
        for (int i = 0; i < getProjectilesList().size(); i++) { //loop through arrayList
            (getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
        }
    }

    Player(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health,
    		double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale, weapon); // calls the constructor in the
        // character super
        // class
    }
}
