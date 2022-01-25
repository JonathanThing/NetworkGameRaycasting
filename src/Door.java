import java.awt.image.BufferedImage;

public class Door extends Environment {
    BufferedImage sprite;
    Door(Vector position, String name, BufferedImage sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, null);
        this.sprite = sprite;
    }
}