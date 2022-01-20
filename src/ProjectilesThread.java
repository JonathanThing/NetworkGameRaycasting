import java.util.ArrayList;

public class ProjectilesThread extends Thread{
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private boolean running = true;
    
    public void addProjectile(Projectile entity) {
        synchronized(projectiles) {
            projectiles.add(entity);
            
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
        System.out.println("This code for ProjectilesThread is running in a thread");
        while(keepRunning()){
            try {
                Thread.sleep(25);
            }catch(Exception e){
                e.printStackTrace();
            }
            synchronized(projectiles) {
                for (Projectile item : projectiles) {
                    item.move();
                    
                }
            }
        }
    }
    
}