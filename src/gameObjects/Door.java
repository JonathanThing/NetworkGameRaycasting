package gameObjects;
import misc.TextureManager;
import util.Const;
import util.Vector;

public class Door extends Environment {
    public Door(Vector position, String name, TextureManager sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, sprite);
    }
}