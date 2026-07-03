package Main.core.model.enemy.state.crystalGuardianState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class GuardianIdleState implements EnemyState {

    private final float VISION_RANGE = 800.0f;
    private final double COOLDOWN_DURATION = 2.5;
    private final double TOTAL_ENRAGED_TIME = 6.0;
    private double cooldownTimer = 0.0;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        cooldownTimer = 0.0;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        cooldownTimer += deltaTime;

        if (cooldownTimer >= COOLDOWN_DURATION && seesKnight(enemy, knightModel)) {

            return new GuardianShootState(TOTAL_ENRAGED_TIME);
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
