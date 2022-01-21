import java.util.ArrayList;
import java.util.ListIterator;

public class ProjectilesThread extends Thread{
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private boolean running = true;
    
    public void addProjectile(Projectile entity) {
        synchronized(projectiles) {
            projectiles.add(entity);
            
        }
    }
    
    public void removeProjectile(Projectile entity) {
        synchronized(projectiles) {
            projectiles.remove(entity);
            
        }
    }
   
    
    public ArrayList<Projectile> getProjectiles(){
        return this.projectiles;
    }
    
    public boolean keepRunning(){
        return this.running == true;
    }
    
    public void stopRunning(){
        this.running = false;
    }
    
    public void run() {
        //Projectile item;
        System.out.println("This code for ProjectilesThread is running in a thread");
        while(keepRunning()){
            try {
                Thread.sleep(5);
            }catch(Exception e){
                e.printStackTrace();
            }
            synchronized(projectiles) {
                
                try {
                    Thread.sleep(5);
                }catch(Exception e){
                    e.printStackTrace();
                }
                for (Projectile item : projectiles) {
                    item.move();

                    if(item.checkCollision(Game.currentLevel) == false){
                        //System.out.println("collision");
                        Game.removeProjectileEntity(item);
                    }
                    
                }
                
            }
        }
    }
    
}