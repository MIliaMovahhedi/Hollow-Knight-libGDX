package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class HuskChargeState implements EnemyState {

    private double timer = 0.0;
    private final double ANTICIPATE_DURATION = 0.4;
    private final double CHARGE_DURATION = 1.0;
    private final double CHARGE_SPEED = 350.0;
    private boolean isCharging = false;

    @Override
    public void enter(EnemyModel enemy) {
        enemy.setXVelocity(0);
        timer = 0.0;
        isCharging = false;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {
        timer += deltaTime;

        if (!isCharging) {

            if (timer >= ANTICIPATE_DURATION) {
                isCharging = true;
                timer = 0.0;
            }
        }
        else {
            enemy.setXVelocity(enemy.isFacingRight() ? CHARGE_SPEED : -CHARGE_SPEED);

            if (timer >= CHARGE_DURATION || isHittingWall(enemy, levelModel) || isAtCliffEdge(enemy, levelModel)) {
                return new HuskCooldownState();
            }
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
    }
}
