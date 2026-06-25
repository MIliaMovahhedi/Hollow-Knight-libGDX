package Main.core.model.knightState;

import Main.core.controller.GameController;
import Main.core.model.GameState;
import Main.core.model.KnightModel;

public class IdleState implements KnightState {

    @Override
    public void enter(KnightModel knight) {

        knight.setXVelocity(0.0);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        if (gameController.isOnLeftMove() || gameController.isOnRightMove()) {

            return new RunState();
        }

        return this;
    }
}
