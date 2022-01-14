import java.awt.image.BufferedImage;
import java.lang.Math;
import java.awt.Graphics;

class Player extends Character {
    
    private int ammo;
    
    public void shoot(){ //BufferedImage sprite) {
        double yComponent = -1* Math.sin(getAngle().getAngleValue());
        double xComponent = -1* Math.cos(getAngle().getAngleValue());
        
        this.getProjectilesList().add(new Projectile(new Vector (this.getPosition().getX(),this.getPosition().getY()), 20, 20, "Bullet", getAngle(), getSprite(), 10,
                                                     10, xComponent, yComponent));
    }
    
    public void moveProjectile() {
        
        for (int i = 0; i < this.getProjectilesList().size(); i++) { //loops through arrayList of projectiles
            
            (getProjectilesList().get(i)).moveDown((getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
            (getProjectilesList().get(i)).moveRight((getProjectilesList().get(i)).getChangeX()); //moves the projectils on the x-axis
            
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
    
    public void drawPlayerProjectile(Graphics g, double offSetX, double offSetY) {
        for (int i = 0; i < getProjectilesList().size(); i++) { //loop through arrayList
            (getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
        }
    }
    
    Player(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
           double speed,
           Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, weapon); // calls the constructor in the
        // character super
        // class
    }
}