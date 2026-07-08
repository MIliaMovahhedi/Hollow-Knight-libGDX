package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.knight.KnightModel;

public class RunState implements KnightState {
    @Override
    public void enter(KnightModel knight) {

        AudioManager.getInstance().loopSfx(AudioManager.getInstance().runSound);
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

    @Override
    public void exit(KnightModel knight) {

        AudioManager.getInstance().stopSfx(AudioManager.getInstance().runSound);
    }
}
