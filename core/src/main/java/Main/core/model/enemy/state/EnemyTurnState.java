package Main.core.model.enemy.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;

public class EnemyTurnState implements EnemyState {

    private double timer = 0.0;
    private final double TURN_DURATION = 0.3;
    private EnemyState nextState;

    public EnemyTurnState(EnemyState nextState) {

        this.nextState = nextState;
    }

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        timer += deltaTime;
        if (timer >= TURN_DURATION) {

            enemy.setFacingRight(!enemy.isFacingRight());
            return nextState;
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }
}
