
/**
 * [Weapon.java]
 * Description: The class for weapons
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 9, 2021
 */
import java.awt.image.BufferedImage;

import java.awt.Graphics;


class Weapon extends GameObject {
  
  //size of the weapon
  private int size;
  
  // damage this weapon deals
  private double damage;
  
  // rate at which this weapon can be used
  private double fireRate;
  
  /**
   * getDamage
   * @return this.damage, the damage this weapon deals
   */
  public double getDamage() {
    return this.damage;
  }
  
  /**
   * getFireRate
   * @return this.fireRate, the rate at which this weapon can fire
   */
  public double getFireRate() {
    return this.fireRate;
  }
  
  /**
   * getSize
   * @return this.size, the size of the weapon
   */
  public int getSize() {
    return this.size;
  }
  
  /**
   * A constructor for the weapons class
   * @param x The x location of the weapon
   * @param y The y location of the weapon
   * @param width The width of the weapon
   * @param height The height of the weapon
   * @param name The name of the object
   * @param sprite The sprite of the weapon
   * @param damage The damage the weapon deals
   * @param fireRate The firerate of the weapon
   * @param size The size of the weapon
   */
  Weapon(int x, int y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate, int size) {
    super(x, y, width, height, name, sprite);
    this.damage = damage;
    this.fireRate = fireRate;
    this.size = size;
  }
  
  /**
   * draw
   * method to draw the healthpack
   * @param g the graphics object, offSetX how much the x is off by, offSetY how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null); //draw the weapon
  }
  
}