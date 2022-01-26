package gameObjects;
import java.util.UUID;

import interfaces.Moveable;
import misc.TextureManager;
import util.Angle;
import util.Vector;

public abstract class Entity extends GameObject implements Moveable {

    private double health;
    private double speed;
    private double spriteZOffset;
    private double spriteScale;
    private UUID uuid;

 Entity(Vector position, int width, int height, Angle angle, TextureManager sprites, double health,
   double speed, double spriteZOffset, double spriteScale) {
  super(position, width, height, angle, sprites);
  this.health = health;
  this.speed = speed;
  this.spriteZOffset = spriteZOffset;
  this.spriteScale = spriteScale;
        this.uuid = UUID.randomUUID();
    }

    public double getSpriteZOffset() {
        return spriteZOffset;
    }

    public void setSpriteZOffset(double spriteZOffset) {
        this.spriteZOffset = spriteZOffset;
    }

    public double getSpriteScale() {
        return spriteScale;
    }

    public void setSpriteScale(double spriteScale) {
        this.spriteScale = spriteScale;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
    
    public void changeHealth(double change) {
        this.health += change;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void moveLeft(double n) {
        this.getPosition().changeX(n);
    }

    public void moveRight(double n) {
        this.getPosition().changeX(-n);
    }

    public void moveUp(double n) {
        this.getPosition().changeY(-n);
    }

    public void moveDown(double n) {
        this.getPosition().changeY(n);
    }

    public UUID getUUID() {
        return uuid;
    }

}