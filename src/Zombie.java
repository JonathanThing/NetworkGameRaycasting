
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Zombie extends Enemy {

  public void move(Player player, Environment[][] b,ArrayList<Enemy> a) {
    
    double xDifference = getX() - player.getX(); 
    double yDifference = getY() - player.getY(); 
    
    double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
    
    double xChange = ((xDifference / hyp) * 3); 
    double yChange = ((yDifference / hyp) * 3); 
    
    this.moveLeft(xChange); 
 
    this.moveUp(yChange); 

  }

  Zombie(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, Weapon weapon) {
    super(x, y, width, height, name, angle, sprite, health, speed, weapon); 
  }  
  
}