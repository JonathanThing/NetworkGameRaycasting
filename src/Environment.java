import java.awt.image.BufferedImage;

public abstract class Environment extends GameObject {

  Environment(Vector position, int width, int height, String name, Angle angle, TextureList sprites) {
    super(position, width, height, name, angle, sprites);

  }
}
