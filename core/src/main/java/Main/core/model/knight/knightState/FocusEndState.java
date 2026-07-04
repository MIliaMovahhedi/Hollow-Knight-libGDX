package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

public class FocusEndState implements KnightState{

    private double timer;

    private final double END_DURATION = 0.3;

    @Override
    public void enter(KnightModel knight) {

        timer = 0.0;
        knight.setXVelocity(0.0);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        timer += deltaTime;

        if (timer >= END_DURATION) {

            if (!knight.isGrounded()) {

                return new FallState();
            }
            return new IdleState();
        }
        return this;
    }
}
