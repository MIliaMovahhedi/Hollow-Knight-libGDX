package Main.core.model.knight.knightState;

import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.knight.KnightModel;

public class AttackState implements KnightState{

    private double timer;
    private boolean isDownAttack;

    public AttackState(boolean isDownAttack) {
        this.isDownAttack = isDownAttack;
    }

    @Override
    public void enter(KnightModel knight) {

        timer = 0.0;
        AudioManager.getInstance().playSfx(AudioManager.getInstance().jumpSound);
    }

    @Override
    public KnightState update(KnightModel knight, GameController gameController, double deltaTime) {

        timer += deltaTime;

        if (timer >= knight.getAttackDuration()) {

            if (!knight.isGrounded()) {

                return new FallState();
            }
            return new IdleState();
        }
        return this;
    }

    @Override
    public void exit(KnightModel knight) {

    }

    public boolean isDownAttack() {
        return isDownAttack;
    }
}
