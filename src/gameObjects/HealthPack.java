package gameObjects;
import misc.TextureManager;
import util.Angle;
import util.Vector;

public class HealthPack extends Item {
    private int value;

    HealthPack(Vector position, int width, int height, Angle angle, TextureManager sprite, int value) {
        super(position, width, height, angle, sprite);
        this.value = value;
    }

    public void onPickup(Player player){
        player.setHealth(player.getHealth() + value);
    }
}