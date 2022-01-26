package gameObjects;
import misc.TextureManager;
import util.Angle;
import util.Vector;

public abstract class Environment extends GameObject {

    Environment(Vector position, int width, int height, String name, Angle angle, TextureManager sprite) {
        super(position, width, height, name, angle, sprite);
    }
}