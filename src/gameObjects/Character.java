package gameObjects;
import java.util.ArrayList;

import misc.TextureManager;
import misc.Weapon;
import util.Angle;
import util.Vector;

public abstract class Character extends Entity implements Runnable {

    private Weapon weapon;
    private ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();

    Character(Vector position, int width, int height, Angle angle, TextureManager sprites, double health,
            double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, angle, sprites, health, speed, spriteZOffset, spriteScale);
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

