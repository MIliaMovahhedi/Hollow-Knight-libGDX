package Main.core.model.enemy.state.mossflyState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class MossflyHiddenState implements EnemyState {

    private final float TRIGGER_RANGE = 350.0f;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        enemy.setYVelocity(0);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knight, LevelModel levelModel, double deltaTime) {

        Rectangle triggerZone = new Rectangle(
            (float) enemy.getX() - TRIGGER_RANGE,
            (float) enemy.getY() - TRIGGER_RANGE,
            TRIGGER_RANGE * 2,
            TRIGGER_RANGE * 2
        );

        Rectangle knightBounds = new Rectangle(
            (float) knight.getX(),
            (float) knight.getY(),
            (float) knight.getWidth(),
            (float) knight.getHeight()
        );


        if (triggerZone.overlaps(knightBounds)) {

            return new MossflyEmergeState();
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {
    }
}
