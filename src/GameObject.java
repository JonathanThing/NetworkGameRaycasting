import java.awt.image.BufferedImage; 
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class GameObject{
  
  private double x;
  private double y;
  
  private int width;
  private int height;
  private BufferedImage sprite;
  private String name;
  private Angle angle;
  
  public double getX(){
    return this.x;
  }

  public double getY(){
    return this.y;
  }

  public void setX(double x){
    this.x = x;
  }
  
  public void setY(double y){
    this.y = y;
  }
  
  public Angle getAngle() {
	  return this.angle;
  }
  
  public int getWidth(){
    return this.width;
  }

  public int getHeight(){
    return this.height;
  }

  public String getName(){
    return this.name;
  }

  public BufferedImage getSprite(){
    return this.sprite;
  }

  public Rectangle getCollision() {
    return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }

  public Rectangle getHitbox() {
    return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }
  
  public abstract void draw (Graphics g,  double offSetX, double offSetY);
  

  GameObject(double x, double y, int width, int height, String name, Angle angle, BufferedImage sprite){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.sprite = sprite;
    this.name = name;
    this.angle = angle;
  }
  
}