import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Environment {

  Wall(Vector position, String name, Angle angle, BufferedImage sprite) {
    super(position, 32, 32, name, angle, sprite);
    
  }

  public boolean playerWin(Player p){
    return true;
  }
}