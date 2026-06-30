package Main.core.model.knightState;

import Main.core.controller.GameController;
import Main.core.model.KnightModel;
import Main.core.model.knightState.*;

public class FallState implements KnightState {

    @Override
    public void enter(KnightModel knight) {}

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        if (gameController.isOnLeftMove()) {

            knight.setXVelocity(-knight.getMaxSpeed());
            knight.setFacingRight(false);
        }
        else if (gameController.isOnRightMove()) {

            knight.setXVelocity(knight.getMaxSpeed());
            knight.setFacingRight(true);
        }
        else {
            knight.setXVelocity(0.0);
        }

        if (gameController.isOnJump() && knight.canDoubleJump()) {

            knight.setCanDoubleJump(false);
            JumpState newState = new JumpState();
            newState.setOnDoubleJump(true);

            return newState;
        }

        if (gameController.isOnDash() && knight.canDash()) {

            return new DashState();
        }

        if (knight.isGrounded()) {

            if (knight.getXVelocity() != 0) {

                return new RunState();
            }

            return new IdleState();
        }

        return this;
    }
}
