import java.awt.image.BufferedImage;

class Zombie extends Enemy {

    Zombie(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, double health,
           double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon);
    }

    public void attack(Player player, BufferedImage sprite, Environment[][] e) {
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
        this.moveRight(hypotenuse.getX());
        this.moveUp(hypotenuse.getY());

        if (detectCollision(e, player)) {
            this.moveLeft(hypotenuse.getX());
            this.moveDown(hypotenuse.getY());
        }

    }

    public boolean detectCollision(Environment[][] e, Player player) {
        for (Environment[] environments : e) {
            for (int j = 0; j < e[0].length; j++) {
                if (environments[j] instanceof Wall) {
                    if (isColliding(environments[j])) {
                        return true;
                    }
                }
            }
        }
        return isColliding(player);
    }

    public void run() {
        // this.moveLeft(100);
        while (keepRunning()) {
            //attack(Game.player, null, Game.map);
            detectCollision(Game.map, Game.player);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // this.moveLeft(-300);
    }

}