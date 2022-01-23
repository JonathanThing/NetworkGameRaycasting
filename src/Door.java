import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Door extends Environment {

  Door(Vector position, String name, BufferedImage sprite) {
    super(position, Const.BOXSIZE, Const.BOXSIZE, name, null, sprite);
    
  }

}