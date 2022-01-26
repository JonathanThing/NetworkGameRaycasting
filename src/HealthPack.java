
public class HealthPack extends Item {
    private int value;

    HealthPack(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, int value) {
        super(position, width, height, name, angle, sprite);
        this.value = value;
    }

    public void onPickup(Player player){
        player.setHealth(player.getHealth() + value);
    }
}