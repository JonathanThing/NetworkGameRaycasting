
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class WinBlock extends Environment {
  
  /**
   * A constructor for the winblock class
   * @param x The x location of the winblock
   * @param y The y location of the winblock
   * @param name The name of the object
   * @param sprite The sprite of the winblock
   */
  WinBlock(double x, double y, String name, BufferedImage sprite) {
    super(x, y, 32, 32, name, sprite); //calls the constructor in the environment super class
    
  }
  
  /**
   * draw
   * A method to draw the winblock
   * @param g the graphics object, 
   * @param offSetX how much the x is off by 
   * @param offSetY how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    g.setColor(Color.YELLOW); //sets the colour to yellow
    g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight()); //draws the winblock square
  }
  
  public void enemyHit(Enemy a) {
  }
  
  public void playerHit(Player p) {
  }
  
  /**
   * playerWin
   * A method to check if the player hits the winblock/wins
   * @param p The player whom we're checking collision with
   * @return A true or false that tells us if the player has collided with the win square
   */
  public boolean playerWin(Player p){ 
    
    //System.out.println("detecting hit");
    
    if (p.getCollision().intersects(this.getHitbox())) {
      
      //System.out.println("hit win");
      
      return true; //if the player has hit the block return true
    }
    
    return false; //otherwise return false
  }
  
}