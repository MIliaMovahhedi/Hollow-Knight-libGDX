package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.model.knight.KnightModel;

public class FocusState implements KnightState {

    private double focusTimer;

    @Override
    public void enter(KnightModel knight) {

        focusTimer = 0.0;
        knight.setXVelocity(0.0);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        if (!gameController.isOnFocus()) {

            return new FocusEndState();
        }

        focusTimer += deltaTime;
        if (focusTimer >= knight.getFocusDuration()) {

            if (knight.getCurrentHealth() < knight.getMaxHealth() && knight.consumeSoul()) {

                knight.heal();
            }
            focusTimer = 0.0;
        }

        return this;
    }
}
