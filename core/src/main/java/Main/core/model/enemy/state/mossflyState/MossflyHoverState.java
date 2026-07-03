package Main.core.model.enemy.state.mossflyState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class MossflyHoverState implements EnemyState {

    private final float SIGHT_RANGE = 400.0f;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        enemy.setYVelocity(0);
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        Rectangle lineOfSight = new Rectangle(

            (float) enemy.getX() - SIGHT_RANGE,
            (float) enemy.getY() - SIGHT_RANGE,
            SIGHT_RANGE * 2,
            SIGHT_RANGE * 2
        );

        Rectangle knightBounds = new Rectangle(

            (float) knightModel.getX(),
            (float) knightModel.getY(),
            (float) knightModel.getWidth(),
            (float) knightModel.getHeight()
        );

        if (lineOfSight.overlaps(knightBounds)) {

            return new MossflyChargeState(knightModel.getX(), knightModel.getY());
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }
}
