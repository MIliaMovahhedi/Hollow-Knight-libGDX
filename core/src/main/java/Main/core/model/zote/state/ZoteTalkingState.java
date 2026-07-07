package Main.core.model.zote.state;

import Main.core.model.zote.ZoteModel;

public class ZoteTalkingState implements ZoteState {

    @Override
    public void enter(ZoteModel zote) {

    }

    @Override
    public void update(ZoteModel zote, float deltaTime) {

    }

    @Override
    public void interact(ZoteModel zote) {

        zote.advanceDialogue();
    }
}
