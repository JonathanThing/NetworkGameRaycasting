  

import java.awt.image.BufferedImage;
import java.awt.Graphics;


class Skeleton extends Enemy {
  
  /**
   * A constructor for the skeleton cla
 x location of the skeleton
   * @param y The y location of the skeleton
   * @param width The width of the skeleton
   * @param height The height of the skeleton
   * @param name The name of the object
   * @param sprite The sprite of the skeleton
   * @param health The health of the skeleton
   * @param weapon The weapon of the skeleton
   */
  Skeleton(double x, double y, int width, int height, String name, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(x, y, width, height, name, sprite, health, speed, weapon); //calls the constructor in the enemy super class
  }
  
  /**
   * shoot
   * method to tells the skeleton when to shoot
   * @param player, the player, sprite, the sprite of the projectile 
   */
  public void attack(Player player) {

  }
  
}