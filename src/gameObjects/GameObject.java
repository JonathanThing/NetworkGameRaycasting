package gameObjects;
import java.awt.*;

import misc.TextureManager;
import util.Angle;
import util.Vector;


public abstract class GameObject {

    private Vector position;

    private int width;
    private int height;
    private TextureManager sprites;
    private Angle angle;

    GameObject(Vector position, int width, int height, Angle angle, TextureManager sprites) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.sprites = sprites;
        this.angle = angle;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Rectangle getHitbox() {
        int x = (int) this.getPosition().getX();
        int y = (int) this.getPosition().getY();
        return new Rectangle(x - this.width / 2, y - this.height / 2, this.width, this.height);
    }

    public Angle getAngle() {
        return this.angle;
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TextureManager getSprites() {
        return this.sprites;
    }

    public boolean isColliding(GameObject other) {
        if (this.getHitbox().intersects(other.getHitbox())) {
            return true;
        }
        return false;
    }

    public void setSprites(TextureManager sprites) {
        this.sprites = sprites;
    }
}