package Main.core.model.enemy.state.crawlidState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.EnemyTurnState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class CrawlidPatrolState implements EnemyState {

    private final double PATROL_SPEED = 80.0;
    private final float EDGE_OFFSET = 5.0f;
    private final float PROBE_SIZE = 2.0f;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(enemy.isFacingRight() ? PATROL_SPEED : -PATROL_SPEED);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        enemy.setXVelocity(enemy.isFacingRight() ? PATROL_SPEED : -PATROL_SPEED);

        if (isHittingWall(enemy, levelModel) || isAtCliffEdge(enemy, levelModel)) {

            return new EnemyTurnState(new CrawlidPatrolState());
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {
    }
}
