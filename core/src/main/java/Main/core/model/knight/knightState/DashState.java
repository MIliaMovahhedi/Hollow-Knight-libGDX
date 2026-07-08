package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.knight.KnightModel;

public class DashState implements KnightState {

    private double timer = 0.0;

    @Override
    public void enter(KnightModel knight) {

        AudioManager.getInstance().playSfx(AudioManager.getInstance().dashSound);

        knight.startDashCooldown();
        knight.setYVelocity(0.0);

        if (knight.isFacingRight()) {

            knight.setXVelocity(knight.getDashSpeed());
        }
        else {

            knight.setXVelocity(-knight.getDashSpeed());
        }
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        timer += deltaTime;

        knight.setYVelocity(-knight.getGravity() * deltaTime);

        if (timer >= knight.getDashDuration()) {

            knight.setXVelocity(0.0);

            if (knight.isGrounded()) {

                if (gameController.isOnLeftMove() || gameController.isOnRightMove())
                    return new RunState();

                return new IdleState();
            }
            else {
                return new FallState();
            }
        }

        return this;
    }

    @Override
    public void exit(KnightModel knight) {

    }
}
