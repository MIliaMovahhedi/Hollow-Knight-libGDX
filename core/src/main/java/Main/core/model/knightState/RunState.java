package Main.core.model.knightState;

import Main.core.controller.GameController;
import Main.core.model.KnightModel;

public class RunState implements KnightState {
    @Override
    public void enter(KnightModel knight) {

    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        if (!gameController.isOnLeftMove() && !gameController.isOnRightMove()) {

            return new IdleState();
        }
        if (gameController.isOnJump() && knight.isGrounded()) {

            return new JumpState();
        }

        if (gameController.isOnLeftMove()) {

            knight.setXVelocity(-knight.getMaxSpeed());
        }
        if (gameController.isOnRightMove()) {

            knight.setXVelocity(knight.getMaxSpeed());
        }

        return this;
    }
}
