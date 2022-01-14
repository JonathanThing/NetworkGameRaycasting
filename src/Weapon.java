import java.awt.image.BufferedImage;

import java.awt.Graphics;


class Weapon {

  private int size;

  private double damage;

  private double fireRate;  

  public double getFireRate() {
    return this.fireRate;
  }

  public int getSize() {
    return this.size;
  }

  Weapon(int x, int y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate, int size) {
    this.damage = damage;
    this.fireRate = fireRate;
    this.size = size;
  }  
}