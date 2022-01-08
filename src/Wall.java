
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Environment {
  
  /**
   * A constructor for the skeleton class
   *n of the skeleton
   * @param y The y location of the skeleton
   * @param name The name of the object
   * @param sprite The sprite of the wall
   */
  Wall(double x, double y, String name, BufferedImage sprite) {
    super(x, y, 32, 32, name, sprite); //calls the constructor in the environment super class
    
  }
  
  /**
   * draw
   * method to draw the wall
   * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null); //draw the wall
    
  }
  
  /**
   * playerWin
   * method to detect if player touches the win block (win be overriden in WinBlock)
   * @param p, the player
   */
  public boolean playerWin(Player p){
    return true;
  }
}
