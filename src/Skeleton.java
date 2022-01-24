
import java.awt.Graphics;

class Skeleton extends Enemy {

	private TextureManager projectileSprites;

	Skeleton(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health,
			double speed, double spriteZOffset, double spriteScale, Weapon weapon, TextureManager projectileSprites) {
		super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale, weapon); 
		this.projectileSprites = projectileSprites;
	}

	public void attack(Player player) {
		this.shoot(player, projectileSprites);
	}

	public void moveProjectile() {
	
    for (Projectile currentProjectile :  this.getProjectilesList()) { //loops through arrayList of projectiles

    	//for some reason it applies to all the projectiles, odd
    	long currentTime = System.currentTimeMillis();       	
    	if (currentProjectile.getSprites().getLastAnimationChange() + 250 <= currentTime) {
    		currentProjectile.getSprites().setLastAnimationChange(currentTime);
    		currentProjectile.getSprites().changeAnimationNumber(1);
    	}
    
    	
    	currentProjectile.moveUp(currentProjectile.getChangeY()); 
    	currentProjectile.moveLeft(currentProjectile.getChangeX()); 

    }
		
		
	}

	public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
		for (int i = 0; i < this.getProjectilesList().size(); i++) { // loop through arrayList
			(this.getProjectilesList().get(i)).draw(g, offSetX, offSetY); // draws the projectile
		}
	}

	public void shoot(Player player, TextureManager sprites) {
		Vector distance = (this.getPosition()).subtract(player.getPosition());
		Vector hypotenuse = distance.normalized();
		this.getProjectilesList().add(new Projectile(new Vector(this.getPosition().getX(), this.getPosition().getY()),
				10, 10, "Bullet", this.getAngle(), sprites, 0, 10, 0, 0.25, -1 * hypotenuse.getX(), hypotenuse.getY()));
	}

}