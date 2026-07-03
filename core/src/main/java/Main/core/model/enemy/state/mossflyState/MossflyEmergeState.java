package Main.core.model.enemy.state.mossflyState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.mossflyState.MossflyHoverState;
import Main.core.model.knight.KnightModel;

public class MossflyEmergeState implements EnemyState {

    private double emergeTimer = 0.0;
    private final double EMERGE_DURATION = 0.8;
    private final double EMERGE_SPEED_Y = 200.0;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        enemy.setYVelocity(EMERGE_SPEED_Y);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knight, LevelModel levelModel, double deltaTime) {

        emergeTimer += deltaTime;


        if (emergeTimer >= EMERGE_DURATION) {

            return new MossflyHoverState();
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setYVelocity(0);
    }
}
