import java.awt.image.BufferedImage;

public abstract class Environment extends GameObject {
  
  Environment(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite) {
    super(x, y, width, height, name, angle, sprite);
    
  }  
}
