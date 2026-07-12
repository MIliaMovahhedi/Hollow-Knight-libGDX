package Main.core.model.enemy.boss.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.boss.FalseKnightModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class OffensiveLeapState implements EnemyState {

    private boolean hasJumped = false;
    private final double jumpSpeedY = 650.0;
    private double jumpSpeedX;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        hasJumped = false;

        if (enemy instanceof FalseKnightModel && ((FalseKnightModel) enemy).isPhaseTwo()) {

            jumpSpeedX = 550.0;
        }
        else {

            jumpSpeedX = 350.0;
        }
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        if (!hasJumped) {

            boolean targetIsRight = knightModel.getX() > enemy.getX();
            enemy.setFacingRight(targetIsRight);
            enemy.setYVelocity(jumpSpeedY);
            enemy.setXVelocity(targetIsRight ? jumpSpeedX : -jumpSpeedX);
            hasJumped = true;
            return this;
        }

        if (enemy.isGrounded() && enemy.getYVelocity() <= 0) {
            return null;
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
    }
}
