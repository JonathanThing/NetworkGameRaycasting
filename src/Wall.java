import java.awt.image.BufferedImage;

public class Wall extends Environment {
    BufferedImage sprite;

    Wall(Vector position, String name, TextureManager sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, sprite);
    }

    Wall(Vector position, String name, BufferedImage sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, null);
        this.sprite = sprite;
    }

    public boolean playerWin(Player p) {
        return true;
    }
}