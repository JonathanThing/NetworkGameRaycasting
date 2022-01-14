import java.awt.image.BufferedImage;

class Skeleton extends Enemy {

  Skeleton(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health,
      double speed, Weapon weapon) {
    super(position, width, height, name, angle, sprite, health, speed, weapon); // calls the constructor in the enemy
                                                                                // super class
  }

  public void attack(Player player) {

  }

}