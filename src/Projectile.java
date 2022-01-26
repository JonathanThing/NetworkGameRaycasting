import java.awt.Graphics;
import java.util.ArrayList;

class Projectile extends Entity {
    
    private double changeX;
    private double changeY;
    private double damage;
    private String shooter;
    
    public String getShooter(){
        return this.shooter;
    }
    
    public double getChangeX() {
        return this.changeX;
    }
    
    public double getChangeY() {
        return this.changeY;
    }
    
    public synchronized boolean checkCollision(LevelE map, ArrayList<Entity> entities){
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                if (map.getMapTile(i, j) instanceof Wall){
                    if  (isColliding(map.getMapTile(i, j))){
                        return true;
                    }
                }
            }
        }
        for (Entity entity : entities) {
            if ((entity instanceof Player) && (getShooter().equals("skeleton"))){
                if(isColliding(entity)){
                	entity.changeHealth(-damage);
                    return true;
                }
            } else if ((entity instanceof Enemy) && (getShooter().equals("player"))){
                if(isColliding(entity)){
                	entity.changeHealth(-damage);
                	if (entity.getHealth() <= 0) {
                		Game.removeCharacterEntity(entity);
                		((Enemy)entity).stopRunning();
                	}
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(Graphics g, double offSetX, double offSetY) {
        g.fillRect((int) (getPosition().getX() - getWidth() / 2 + offSetX), (int) (getPosition().getY() - getHeight() / 2 + offSetY), getWidth(), getHeight()); //draw the projectile
    }
    
    public void move(){
        moveUp(this.getChangeY() * getSpeed());
        moveLeft(this.getChangeX() * getSpeed());
        
        long currentTime = System.currentTimeMillis();       	
    	if (this.getSprites().getLastAnimationChange() + 250 <= currentTime) {
    		this.getSprites().setLastAnimationChange(currentTime);
    		this.getSprites().changeAnimationNumber(1);
    	}
    }
    
    
    Projectile(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, 
               double health, double speed, double spriteZOffset, double spriteScale, double changeX, double changeY, String shooter, double damage) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale);
        this.changeX = changeX;
        this.changeY = changeY;
        this.shooter = shooter;
        this.damage = damage;
    }
    
}