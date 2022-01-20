import java.awt.image.BufferedImage;

class Zombie extends Enemy {
    
    Zombie(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon); 
    }  
    
    public void attack(Player player, BufferedImage sprite){
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
        this.moveLeft(-hypotenuse.getX());      
        this.moveUp(hypotenuse.getY()); 
        
    }
    
    public void run(){
        System.out.println("zombie starting");
        //this.moveLeft(100);
        while(keepRunning()){
            attack(Game.player, Game.sprites.getSingleTexture(0));
            try {
                Thread.sleep(20);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //this.moveLeft(-300);
    }
    
}