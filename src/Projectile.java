import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Projectile extends Entity {
    
    private double changeX;
    private double changeY;
    
    public double getChangeX() {
        return this.changeX;
    }
    
    public double getChangeY() {
        return this.changeY;
    }
    
    public synchronized boolean checkCollision(Level map){
        // door collision
        
         Vector movementVector = new Vector(getChangeX(), getChangeY());
         Vector futurePosition = this.getPosition().add(movementVector.multiplyByScalar(4));
         
        if (map.getMapTile((int) futurePosition.getY() / Const.BOXSIZE,
                           (int) futurePosition.getX() / Const.BOXSIZE) == 4) {
            return true;
        }
        
        // wall side collision
        futurePosition = this.getPosition().add(movementVector.multiplyByScalar(2));
        if (map.getMapTile((int) this.getPosition().getY() / Const.BOXSIZE,
                           (int) futurePosition.getX() / Const.BOXSIZE) < 1) {
            //this.moveLeft(movementVector.getX());
            return true;
        }
        
        // wall forward collision
        if (map.getMapTile((int) futurePosition.getY() / Const.BOXSIZE,
                           (int) this.getPosition().getX() / Const.BOXSIZE) < 1) {
            //this.moveDown(movementVector.getY());
            return true;
        }
        
        return false;
    }
    
    public void draw(Graphics g, double offSetX, double offSetY) {
        //System.out.println("drawing projectile");
        g.fillRect((int) (getPosition().getX() - getWidth() / 2 + offSetX), (int) (getPosition().getY() - getHeight() / 2 + offSetY), getWidth(), getHeight()); //draw the projectile
    }
    
    public void move(){
        moveUp(this.getChangeY() * getSpeed());
        moveLeft(this.getChangeX() * getSpeed());
    }
    
    
    Projectile(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, 
               double health, double speed, double spriteZOffset, double spriteScale, double changeX, double changeY) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale);
        this.changeX = changeX;
        this.changeY = changeY;
    }
    
}