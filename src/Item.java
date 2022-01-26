import java.awt.*;

public class Item extends GameObject implements Pickupable {

    Item(Vector position, int width, int height, String name, Angle angle, TextureManager sprite) {
        super(position, width, height, name, angle, sprite);
    }

    public void draw(Graphics g) {
        g.fillRect((int) this.getPosition().getX(), (int) this.getPosition().getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onPickup() {
    }
}