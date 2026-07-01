package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

public class FocusState implements KnightState {

    private double focusTimer;
    private final double focusDuration = 1.5;

    @Override
    public void enter(KnightModel knight) {

        focusTimer = 0.0;
        knight.setXVelocity(0.0);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        if (!gameController.isOnFocus()) {

            return new IdleState();
        }

        focusTimer += deltaTime;
        if (focusTimer >= focusDuration) {

            if (knight.getCurrentHealth() < knight.getMaxHealth() && knight.consumeSoul()) {

                knight.heal();
            }
            focusTimer = 0.0;
        }

        return this;
    }
}
