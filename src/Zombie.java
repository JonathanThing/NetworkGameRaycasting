import java.awt.image.BufferedImage;

class Zombie extends Enemy {
    
    Zombie(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, weapon); 
    }  
    
    public void attack(Player player){
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
              
        this.moveLeft(-1 *hypotenuse.getX());      
        this.moveUp(hypotenuse.getY()); 
    }
    
}