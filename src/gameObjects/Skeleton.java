package gameObjects;
import java.awt.image.BufferedImage;

import core.Game;
import misc.Level;
import misc.TextureManager;
import misc.Weapon;
import util.Angle;
import util.Vector;

import java.awt.Graphics;

public class Skeleton extends Enemy {
    
    public Skeleton(Vector position, int width, int height, String name, Angle angle, TextureManager sprite, 
             double health, double speed, double spriteZOffset, double spriteScale, Weapon weapon) {
        super(position, width, height, name, angle, sprite, health, speed, spriteZOffset, spriteScale, weapon); //calls the constructor in the enemy super class
    }
    
    
    public void attack(Player player, TextureManager sprite, Level e) {
        this.shoot(player, sprite); 
    }
    
    public void shoot(Player player, TextureManager sprite) {
        Vector distance = (this.getPosition()).subtract(player.getPosition());
        Vector hypotenuse = distance.normalized();
        Game.addProjectileEntity(new Projectile(new Vector (this.getPosition().getX(),this.getPosition().getY()), 10, 10, "Bullet", this.getAngle().clone(), sprite, 0 , 1 , 0,
                                                0.25,-1* hypotenuse.getX(), hypotenuse.getY(), "skeleton", 10));
    }
    
    public void run(){
        int count = 0;
        while(keepRunning()){
            
            if (count == 100){            
                attack(Game.player, Game.fireBall, Game.map);
                count = 0;
            }
            count++;
            //moveProjectile();
            
            try {
                Thread.sleep(25);
            }catch(Exception e){
                e.printStackTrace();
            }
        }                              
    }

    @Override
    public void attack(Player player, BufferedImage sprite, Level map) {
        // TODO Auto-generated method stub
    }
}