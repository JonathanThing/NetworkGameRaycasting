
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Environment {

  Wall(double x, double y, String name, Angle angle, BufferedImage sprite) {
    super(x, y, 32, 32, name, angle, sprite);
    
  }

  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null); //draw the wall
    
  }

  public boolean playerWin(Player p){
    return true;
  }
}
