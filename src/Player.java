/**
 * [Player.java]
 * Description: The class for the player
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 28, 2021
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character {
  
	private int ammo;

  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);
  }

  public void movement(boolean up, boolean down, boolean left, boolean right, ArrayList<Enemy> list,
                       Environment[][] map) {
    double xMove = 0; 
    double yMove = 0;
    
    if (up) {
      yMove += 1;
    }
    
    if (down) {
      yMove -= 1; 
    }
    
    if (left) {
      xMove -= 1;
    }
    
    if (right) {
      xMove += 1;
    }
    
    double hyp = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2)); 
    
    if (hyp != 0) { 
      
      this.moveRight((xMove / hyp) * 5);
         
      this.moveUp((yMove / hyp) * 5);

    }
    
  }
  
  public Rectangle getAggro() {
    return new Rectangle((int)this.getX() - (32*50)/2, (int) this.getY() - (32*35)/2, 32*50, 32*35);
  }

  Player(double x, double y, int width, int height, String name, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(x, y, width, height, name, sprite, health, speed, weapon); //calls the constructor in the character super class
  }
  
}