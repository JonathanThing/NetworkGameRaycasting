  

import java.awt.image.BufferedImage;
import java.awt.Graphics;


class Skeleton extends Enemy {
  
  Skeleton(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(x, y, width, height, name, angle, sprite, health, speed, weapon); //calls the constructor in the enemy super class
  }

  public void attack(Player player) {

  }
  
}