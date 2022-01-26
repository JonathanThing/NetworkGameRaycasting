package gameObjects;
import misc.TextureManager;
import util.Angle;
import util.Vector;

public abstract class Environment extends GameObject {

    Environment(Vector position, int width, int height, Angle angle, TextureManager sprite) {
        super(position, width, height, angle, sprite);
    }
}