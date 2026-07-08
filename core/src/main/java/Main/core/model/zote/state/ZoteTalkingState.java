package Main.core.model.zote.state;

import Main.core.controller.manager.AudioManager;
import Main.core.model.zote.ZoteModel;
import com.badlogic.gdx.math.MathUtils;

public class ZoteTalkingState implements ZoteState {

    @Override
    public void enter(ZoteModel zote) {

        int randomIndex = MathUtils.random(0, 4);
        AudioManager.getInstance().playSfx(AudioManager.getInstance().zoteTalkingSounds[randomIndex]);
    }

    @Override
    public void update(ZoteModel zote, float deltaTime) {

    }

    @Override
    public void interact(ZoteModel zote) {

        zote.advanceDialogue();
    }

    @Override
    public void exit(ZoteModel zote) {

    }
}
