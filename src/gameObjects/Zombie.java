package gameObjects;
import java.awt.image.BufferedImage;

import core.Game;
import misc.Level;
import misc.TextureManager;
import misc.Weapon;
import util.Angle;
import util.Vector;

public class Zombie extends Enemy {

    public Zombie(Vector position, int width, int height, Angle angle, TextureManager sprite, double health,
            double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon);
    }

    public void attack(Player player, BufferedImage sprite, Level map) {
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
        this.moveRight(hypotenuse.getX());
        this.moveUp(hypotenuse.getY());

        if (detectCollision(map, player)) {
            this.moveLeft(hypotenuse.getX());
            this.moveDown(hypotenuse.getY());
        }

    }

    public boolean detectCollision(Level map, Player player) {
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                if (map.getMapTile(i, j) instanceof Wall) {
                    if (isColliding(map.getMapTile(i, j))) {
                        return true;
                    }
                }
            }
        }
        if (isColliding(player)) {
            return true;
        }
        return false;
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