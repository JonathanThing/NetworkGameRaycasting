package threads;
import java.util.UUID;

import gameObjects.Character;

public class CharacterThread extends Thread {
    private UUID entityUUID;

    public CharacterThread(Character character) {
        super(character);
        entityUUID = character.getUUID();
    }
}