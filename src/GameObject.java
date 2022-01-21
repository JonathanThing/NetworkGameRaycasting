import java.awt.image.BufferedImage;
import java.awt.Rectangle;

abstract class GameObject {
  
  private Vector position;
  
  private int width;
  private int height;
  private BufferedImage sprite;
  private String name;
  private Angle angle;
  private Rectangle hitbox;
  
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
  
  public Rectangle getHitbox() {
      int x  = (int)this.getPosition().getX();
      int y  = (int)this.getPosition().getY();
      return new Rectangle(x - this.width/2, y - this.height/2, this.width, this.height);
  } 
  
  public BufferedImage getSprite() {
    return this.sprite;
  }
  
  public boolean isColliding(GameObject other) {
    if(this.getHitbox().intersects(other.getHitbox())){
      return true;
    }
    return false;
  }
  
  GameObject(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite) {
    this.position = position;
    this.width = width;
    this.height = height;
    this.sprite = sprite;
    this.name = name;
    this.angle = angle;
    
    this.hitbox = new Rectangle((int)position.getX(),(int)position.getY(),this.width,this.height);
  }
  
}