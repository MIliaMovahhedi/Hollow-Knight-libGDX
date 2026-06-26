package Main.core.model.knightState;

import Main.core.controller.GameController;
import Main.core.model.KnightModel;

public class DashState implements KnightState {

    private double timer = 0.0;

    @Override
    public void enter(KnightModel knight) {

        knight.setCanDash(false);
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

            if (knight.isFacingRight()) {

                return new IdleState();
            }

            return new FallState();
        }

        return this;
    }
}
