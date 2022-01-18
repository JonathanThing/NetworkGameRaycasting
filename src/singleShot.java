import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class singleShot extends Weapon{

	singleShot(double x, double y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate,
			int size, int clipSize) {
		super(x, y, width, height, name, sprite, damage, fireRate, size, clipSize);
	}

	void shoot(Angle angle, Vector playerPos, ArrayList<Projectile> projectilesList) {
		double yComponent = -1* Math.sin(angle.getAngleValue());
		double xComponent = -1* Math.cos(angle.getAngleValue());		
		projectilesList.add(new Projectile(new Vector(playerPos.getX(),playerPos.getY()), 20, 20, "Bullet", angle, null, 10, 10, xComponent, yComponent));
	}
	
	void moveProjectile(ArrayList<Projectile> projectilesList) {
		  for(int i = 0; i < projectilesList.size(); i++) {
				(projectilesList.get(i)).moveUp((projectilesList.get(i).getChangeY()*-i)/1.5); //moves the projectils on the y-axis
		        (projectilesList.get(i)).moveLeft((projectilesList.get(i).getChangeX()*-i)/1.5); //moves the projectils on the x-axis   
		  }
	}
	
}
 
