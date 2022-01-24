import java.awt.Graphics;
import java.util.ArrayList;

class Projectile extends Entity {
    
    private double changeX;
    private double changeY;
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
    
    public synchronized boolean checkCollision(Environment[][] e, ArrayList<Entity> entities){
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e[0].length; j++) {
                if (e[i][j] instanceof Wall){
                    if  (isColliding(e[i][j])){
                        System.out.println("hit wall");
                        return true;
                    }
                }
            }
        }
        for (int i = 0; i < entities.size(); i++){
            if ((entities.get(i) instanceof Player) && (getShooter().equals("skeleton"))){
                if(isColliding(entities.get(i))){
                    System.out.println("hit player");
                    return true;
                }
            } else if ((entities.get(i) instanceof Enemy) && (getShooter().equals("player"))){
                if(isColliding(entities.get(i))){
                    System.out.println("hit enemy");
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
     public synchronized boolean checkEnemyCollision(){
     
     }
     */
    
    public void draw(Graphics g, double offSetX, double offSetY) {
        //System.out.println("drawing projectile");
        g.fillRect((int) (getPosition().getX() - getWidth() / 2 + offSetX), (int) (getPosition().getY() - getHeight() / 2 + offSetY), getWidth(), getHeight()); //draw the projectile
    }
    
    public void move(){
        moveUp(this.getChangeY() * getSpeed());
        moveLeft(this.getChangeX() * getSpeed());
    }
    
    
    Projectile(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, 
               double health, double speed, double spriteZOffset, double spriteScale, double changeX, double changeY, String shooter) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale);
        this.changeX = changeX;
        this.changeY = changeY;
        this.shooter = shooter;
    }
    
}