import java.awt.image.BufferedImage;
import java.awt.Rectangle;

abstract class GameObject {

 private Vector position;

 private int width;
 private int height;
 private TextureManager sprites;
 private String name;
 private Angle angle;

 public Vector getPosition() {
  return position;
 }

 public void setPosition(Vector position) {
  this.position = position;
 }

 public void setWidth(int width) {
  this.width = width;
 }

 public void setHeight(int height) {
  this.height = height;
 }

 public void setSprites(TextureManager sprites) {
  this.sprites = sprites;
 }

 public void setName(String name) {
  this.name = name;
 }

 public void setAngle(Angle angle) {
  this.angle = angle;
 }

 public Rectangle getCollision() {
    int x  = (int)this.getPosition().getX();
    int y  = (int)this.getPosition().getY();
    return new Rectangle(x - this.width/2, y - this.height/2, this.width, this.height);
 } 

 public Angle getAngle() {
  return this.angle;
 }

 public int getWidth() {
  return this.width;
 }

 public int getHeight() {
  return this.height;
 }

 public String getName() {
  return this.name;
 }

 public TextureManager getSprites() {
  return this.sprites;
 }

 GameObject(Vector position, int width, int height, String name, Angle angle, TextureManager sprites) {
  this.position = position;
  this.width = width;
  this.height = height;
  this.sprites = sprites;
  this.name = name;
  this.angle = angle;
 }

}