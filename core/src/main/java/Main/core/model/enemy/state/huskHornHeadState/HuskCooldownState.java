package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class HuskCooldownState implements EnemyState {

    private double timer = 0.0;
    private final double COOLDOWN_DURATION = 0.8;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        timer += deltaTime;

        if (timer >= COOLDOWN_DURATION) {

            return new HuskPatrolState();
        }
        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }
}
