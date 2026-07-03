package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class HuskRestState implements EnemyState {

    private final double REST_DURATION = 1.5;
    private final float VISION_RANGE = 250.0f;
    private double restTimer = 0.0;

    @Override
    public void enter(EnemyModel enemy) {
        enemy.setXVelocity(0);
        restTimer = 0.0;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        restTimer += deltaTime;

        if (seesKnight(enemy, knightModel)) {
            return new HuskChargeState();
        }

        if (restTimer >= REST_DURATION) {
            return new HuskPatrolState();
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }

    private boolean seesKnight(EnemyModel enemy, KnightModel knight) {

        float visionX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth())
            : (float) (enemy.getX() - VISION_RANGE);

        Rectangle visionBounds = new Rectangle(
            visionX,
            (float) enemy.getY(),
            VISION_RANGE,
            (float) enemy.getHeight()
        );

        Rectangle knightBounds = new Rectangle(
            (float) knight.getX(),
            (float) knight.getY(),
            (float) knight.getWidth(),
            (float) knight.getHeight()
        );

        return visionBounds.overlaps(knightBounds);
    }
}
