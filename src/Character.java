import java.util.ArrayList;

abstract class Character extends Entity implements Runnable {

    private final ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
    private Weapon weapon;

    Character(Vector position, int width, int height, String name, Angle angle, TextureManager sprites, double health,
              double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprites, health, speed, spriteZOffset, spriteScale);
        this.weapon = weapon;

    }

    public ArrayList<Projectile> getProjectilesList() {
        return projectilesList;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}