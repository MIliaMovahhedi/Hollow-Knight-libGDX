package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

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
        if (gameController.isOnDash() && knight.canDash()) {

            return new DashState();
        }
        if (gameController.isOnJump()){

            return new JumpState();
        }

        return this;
    }
}
