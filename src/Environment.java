import java.awt.image.BufferedImage;

public abstract class Environment extends GameObject {

  Environment(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite) {
    super(position, width, height, name, angle, sprite);

  }
}