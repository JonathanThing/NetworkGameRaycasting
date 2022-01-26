package gameObjects;
import java.awt.Graphics;

import interfaces.Pickupable;
import misc.TextureManager;
import util.Angle;
import util.Vector;


public class Item extends GameObject implements Pickupable {

    Item(Vector position, int width, int height, Angle angle, TextureManager sprite) {
        super(position, width, height, angle, sprite);
    }

    public void draw(Graphics g) {
        g.fillRect((int) this.getPosition().getX(), (int) this.getPosition().getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onPickup() {
    }
}