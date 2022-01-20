import java.util.UUID;
public class EntityThread extends Thread{
    private UUID entityUUID;
    
     EntityThread(Entity entity) {
        super(entity);
        entityUUID = entity.getUUID();
     }
}