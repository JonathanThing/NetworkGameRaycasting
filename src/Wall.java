import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Environment {

  Wall(Vector position, String name, BufferedImage sprite) {
    super(position, Const.BOXSIZE, Const.BOXSIZE, name, null, sprite);
    
  }

  public boolean playerWin(Player p){
    return true;
  }
}