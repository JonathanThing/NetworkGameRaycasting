
class Zombie extends Enemy {
    
    Zombie(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health, double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale, weapon); 
    }  
    
    public void attack(Player player){
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
             
        this.moveLeft(-hypotenuse.getX());      
        this.moveUp(hypotenuse.getY()); 
        
    }
    
}