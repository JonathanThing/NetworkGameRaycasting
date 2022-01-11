import java.awt.image.BufferedImage;

abstract class GameObject {

 private Vector position;

 private int width;
 private int height;
 private BufferedImage sprite;
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

 public void setSprite(BufferedImage sprite) {
  this.sprite = sprite;
 }

 public void setName(String name) {
  this.name = name;
 }

 public void setAngle(Angle angle) {
  this.angle = angle;
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

 public BufferedImage getSprite() {
  return this.sprite;
 }

 GameObject(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite) {
  this.position = position;
  this.width = width;
  this.height = height;
  this.sprite = sprite;
  this.name = name;
  this.angle = angle;
 }

}