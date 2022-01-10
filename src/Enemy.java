import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;

abstract class Enemy extends Character {

  Enemy(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(position, width, height, name, angle, sprite, health, speed, weapon);
  }
  
}