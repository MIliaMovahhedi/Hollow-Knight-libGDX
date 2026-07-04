package Main.core.model.enemy.boss.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class ChargeState implements EnemyState {

    private double timer = 0.0;
    private final double windupDuration = 0.4;
    private final double chargeDuration = 1.2;
    private final double chargeSpeed = 450.0;
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

            boolean targetIsRight = knightModel.getX() > enemy.getX();
            enemy.setFacingRight(targetIsRight);

            if (timer >= windupDuration) {

                isCharging = true;
                timer = 0.0;
            }
        }
        else {

            enemy.setXVelocity(enemy.isFacingRight() ? chargeSpeed : -chargeSpeed);

            if (timer >= chargeDuration || isHittingWall(enemy, levelModel)) {
                return null;
            }
        }
        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
    }
}
