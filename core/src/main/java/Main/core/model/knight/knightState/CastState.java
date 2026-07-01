package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

public class CastState implements KnightState {

    private double castTimer;
    private final double castDuration = 0.8;
    private boolean isUpwardCast;

    public CastState(boolean isUpwardCast) {

        this.isUpwardCast = isUpwardCast;
    }

    @Override
    public void enter(KnightModel knight) {

        castTimer = 0.0;

        if (isUpwardCast) {

            knight.setXVelocity(0.0);
            knight.setYVelocity(-50.0);
        }
        else {

            knight.setXVelocity(knight.isFacingRight() ? -30.0 : + 30.0);
            knight.setYVelocity(0.0);
        }
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        castTimer += deltaTime;
        if (castTimer >= castDuration) {

            if (!knight.isGrounded()) {

                return new FallState();
            }
            return new IdleState();
        }

        return this;
    }

    @Override
    public double getGravityScale() {
        return 0.0;
    }

    public boolean isUpwardCast() {
        return isUpwardCast;
    }
}
