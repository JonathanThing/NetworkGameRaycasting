import java.awt.image.BufferedImage;

abstract class Enemy extends Character {
    
    Enemy(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
    		double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon);
    }

    public abstract void attack(Player player);
    
    
    
}