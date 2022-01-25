import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Skeleton extends Enemy {
    
    Skeleton(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, 
             double health, double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon); //calls the constructor in the enemy super class
    }
    
    
    public void attack(Player player, TextureManager sprite, Environment[][] e) {
        this.shoot(player, sprite); 
    }
    
    
    public void moveProjectile() {    
        for (int i = 0; i < this.getProjectilesList().size(); i++) { //loops through arrayList of projectiles      
            (this.getProjectilesList().get(i)).moveUp((this.getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
            (this.getProjectilesList().get(i)).moveLeft((this.getProjectilesList().get(i)).getChangeX()); //moves the projectils on the x-axis        
        }      
    }
    
    
    public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
        for (int i = 0; i < this.getProjectilesList().size(); i++) { //loop through arrayList
            (this.getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
        }
    }
    
    public void shoot(Player player, TextureManager sprite) {
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
        Game.addProjectileEntity(new Projectile(new Vector (this.getPosition().getX(),this.getPosition().getY()), 10, 10, "Bullet", this.getAngle(), sprite, 0 ,0.5 , 0,
                                                1,-1* hypotenuse.getX(), hypotenuse.getY(), "skeleton"));
    }
    
    public void run(){
        int count = 0;
        while(keepRunning()){
            
            if (count == 100){            
                attack(Game.player, Game.fireBall, Game.map);
                count = 0;
            }
            count++;
            //moveProjectile();
            
            try {
                Thread.sleep(25);
            }catch(Exception e){
                e.printStackTrace();
            }
        }                              
    }

    @Override
    public void attack(Player player, BufferedImage sprite, Environment[][] e) {
        // TODO Auto-generated method stub
    }
}