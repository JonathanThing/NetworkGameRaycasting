
import java.awt.image.BufferedImage;
import java.awt.Graphics;


class Skeleton extends Enemy {
  
  Skeleton(Vector position, int width, int height, String name, Angle angle, BufferedImage sprite, double health, double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
    super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon); //calls the constructor in the enemy super class
  }

  public void attack(Player player, BufferedImage sprite) {
      this.shoot(player, sprite); 
  }
  
  public void moveProjectile() {    
      for (int i = 0; i < this.getProjectilesList().size(); i++) { //loops through arrayList of projectiles      
          (this.getProjectilesList().get(i)).moveUp((this.getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
          (this.getProjectilesList().get(i)).moveLeft((this.getProjectilesList().get(i)).getChangeX()); //moves the projectils on the x-axis        
      }      
  }
  
  public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
      for (int i = 0; i < this.getProjectilesList().size(); i++) { //loop through arrayList
          (this.getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
      }
  }
  
  public void shoot(Player player, BufferedImage sprite) {
      Vector distance = (this.getPosition()).subtract(player.getPosition());
      Vector hypotenuse = distance.normalized();
      this.getProjectilesList().add(new Projectile(new Vector (this.getPosition().getX(),this.getPosition().getY()), 10, 10, "Bullet", this.getAngle(), sprite, 0 ,5 , 0,
      1,-1* hypotenuse.getX(), hypotenuse.getY()));
  }
  
}