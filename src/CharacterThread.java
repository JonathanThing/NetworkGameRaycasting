import java.util.UUID;

public class CharacterThread extends Thread {

    CharacterThread(Character character) {
        super(character);
        UUID entityUUID = character.getUUID();
    }
}