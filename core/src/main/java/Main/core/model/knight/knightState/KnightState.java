package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

public interface KnightState {

    void enter(KnightModel knight);
    KnightState update(KnightModel knight, GameController  gameController, double deltaTime);
    void exit(KnightModel knight);
    default double getGravityScale() {

        return 1;
    }
}
