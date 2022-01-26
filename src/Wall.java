import java.awt.image.BufferedImage;

public class Wall extends Environment {

    Wall(Vector position, String name, TextureManager sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, sprite);
    }

    public boolean playerWin(Player p) {
        return true;
    }
}