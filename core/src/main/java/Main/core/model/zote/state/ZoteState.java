package Main.core.model.zote.state;

import Main.core.model.zote.ZoteModel;

public interface ZoteState {

    void enter(ZoteModel zote);
    void update(ZoteModel zote, float deltaTime);
    void interact(ZoteModel zote);
}
