package Main.core.model.knightState;

import Main.core.controller.GameController;
import Main.core.model.KnightModel;

public interface KnightState {

    void enter(KnightModel knight);
    KnightState update(KnightModel knight, GameController  gameController, double deltaTime);
    default double getGravityScale() {

        return 1;
    }
}
