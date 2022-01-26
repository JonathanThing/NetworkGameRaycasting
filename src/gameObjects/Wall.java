package gameObjects;

import misc.TextureManager;
import util.Const;
import util.Vector;

public class Wall extends Environment {

    public Wall(Vector position, String name, TextureManager sprite) {
        super(position, Const.BOX_SIZE, Const.BOX_SIZE, name, null, sprite);
    }

    public boolean playerWin(Player p) {
        return true;
    }
}