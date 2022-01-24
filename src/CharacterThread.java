import java.util.UUID;

public class CharacterThread extends Thread {
    private UUID entityUUID;

    CharacterThread(Character character) {
        super(character);
        entityUUID = character.getUUID();
    }
}