package Main.core.model.enemy.boss.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class StunState implements EnemyState {

    private double timer = 0.0;
    private final double stunDuration = 5.5;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        timer = 0.0;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        timer += deltaTime;

        if (timer >= stunDuration) {
            return null;
        }
        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }
}
