package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

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
        if (gameController.isOnDash() && knight.canDash()) {
            return new DashState();
        }

        if (gameController.isOnLeftMove()) {

            knight.setXVelocity(-knight.getMaxSpeed());
            knight.setFacingRight(false);
        }
        if (gameController.isOnRightMove()) {

            knight.setXVelocity(knight.getMaxSpeed());
            knight.setFacingRight(true);
        }

        return this;
    }
}
