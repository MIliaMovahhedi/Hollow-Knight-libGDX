package Main.core.model.enemy.boss.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.boss.FalseKnightModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class ShockWaveLandingState implements EnemyState {

    private boolean hasJumped = false;
    private boolean hasLanded = false;
    private double recoveryTimer = 0.0;
    private double RECOVERY_TIME = 0.5;
    private final double jumpSpeedY = 750.0;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        hasJumped = false;
        hasLanded = false;
        recoveryTimer = 0.0;

        if (enemy instanceof FalseKnightModel && ((FalseKnightModel) enemy).isPhaseTwo()) {

            RECOVERY_TIME = 0.3;
        }
        else {

            RECOVERY_TIME = 0.5;
        }
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        if (!hasJumped) {

            boolean targetIsRight = knightModel.getX() > enemy.getX();
            enemy.setFacingRight(targetIsRight);
            enemy.setYVelocity(jumpSpeedY);
            enemy.setXVelocity(targetIsRight ? 200.0 : -200.0);
            hasJumped = true;
            return this;
        }

        if (!hasLanded) {

            if (enemy.isGrounded() && enemy.getYVelocity() <= 0) {

                hasLanded = true;
                enemy.setXVelocity(0);

                if (enemy instanceof FalseKnightModel) {

                    ((FalseKnightModel) enemy).triggerShockWaves();
                }
            }
        }
        else {

            recoveryTimer += deltaTime;
            if (recoveryTimer >= RECOVERY_TIME) {

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
