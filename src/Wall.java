import java.awt.image.BufferedImage;

public class Wall extends Environment {

  Wall(Vector position, String name, Angle angle, TextureList sprites) {
    super(position, 32, 32, name, angle, sprites);

  }

  public boolean playerWin(Player p) {
    return true;
  }
}
