package Main.core.controller.manager;

import Main.core.model.SaveData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SaveManager {

    private static SaveManager instance;
    private final Json json;
    private final String saveDirectory = "saves/";

    private SaveManager() {
        json = new Json();
    }

    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    public void saveGame(SaveData saveData, int slotIndex) {

        FileHandle file = Gdx.files.local(saveDirectory + "slot_" + slotIndex + ".json");
        String saveString = json.toJson(saveData);
        file.writeString(saveString, false);
    }

    public SaveData loadGame(int slotIndex) {
        FileHandle file = Gdx.files.local(saveDirectory + "slot_" + slotIndex + ".json");
        if (file.exists()) {
            return json.fromJson(SaveData.class, file.readString());
        }
        return null;
    }

    public boolean isSlotEmpty(int slotIndex) {
        FileHandle file = Gdx.files.local(saveDirectory + "slot_" + slotIndex + ".json");
        return !file.exists();
    }

    public void deleteSlot(int slotIndex) {
        FileHandle file = Gdx.files.local(saveDirectory + "slot_" + slotIndex + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}
