package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.knight.KnightModel;

public class DeathState implements KnightState {

    private double timer = 0.0;
    private final double DEATH_DURATION = 2.0;

    @Override
    public void enter(KnightModel knight) {

        knight.setXVelocity(0.0);
        AudioManager.getInstance().playSfx(AudioManager.getInstance().deathSound);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        timer += deltaTime;
        if (timer >= DEATH_DURATION) {
            knight.respawnAfterDeath();
            return new IdleState();
        }
        return this;
    }

    @Override
    public void exit(KnightModel knight) {

    }
}
