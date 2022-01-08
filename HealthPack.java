import java.awt.image.BufferedImage;
import java.awt.Graphics;

class HealthPack extends GameObject {

  HealthPack(int x, int y, int width, int height, String name, BufferedImage sprite) {
    super(x, y, width, height, name, sprite);
  }

  public void draw(Graphics g, double offSetX, double offSetY) { 
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);
  }

  public boolean checkCollision(Player p) {
    if (this.getCollision().intersects(p.getCollision())) {
      return true;
    }
    
    return false;
    
  }
  
}