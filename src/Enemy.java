import java.awt.image.BufferedImage;

abstract class Enemy extends Character {

    private boolean running = true;

    Enemy(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health,
          double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale, weapon);
    }

    public boolean keepRunning() {
        return this.running;
    }

    public void stopRunning() {
        this.running = false;
    }

    public abstract void attack(Player player, BufferedImage sprite, Environment[][] e);

}