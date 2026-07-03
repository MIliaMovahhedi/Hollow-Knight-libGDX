package Main.core.model.enemy.state.crystalGuardianState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class GuardianEnragedState implements EnemyState {

    private final double CHARGE_SPEED = 300.0;
    private final double FAST_COOLDOWN = 1.5;
    private final float INFINITE_RANGE = 10000.0f;
    private double enragedTimer;
    private double cooldownTimer = 0.0;

    public GuardianEnragedState(double remainingEnrageTime) {

        this.enragedTimer = remainingEnrageTime;
    }

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        cooldownTimer = 0.0;
        enragedTimer = 0.0;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        enragedTimer -= deltaTime;
        cooldownTimer += deltaTime;

        if (enragedTimer <= 0) {

            return new GuardianIdleState();
        }

        enemy.setFacingRight(knightModel.getX() > enemy.getX());
        enemy.setXVelocity(enemy.isFacingRight() ? CHARGE_SPEED : -CHARGE_SPEED);

        if (cooldownTimer >= FAST_COOLDOWN && seesKnight(enemy, knightModel)) {

            return new GuardianShootState(enragedTimer);
        }

        if (isHittingWall(enemy, levelModel, deltaTime)) {

            enemy.setXVelocity(0);
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }

    private boolean seesKnight(EnemyModel enemy, KnightModel knight) {

        float visionX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth())
            : (float) (enemy.getX() - INFINITE_RANGE);

        Rectangle visionBounds = new Rectangle(
            visionX,
            (float) enemy.getY(),
            INFINITE_RANGE,
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

    private boolean isHittingWall(EnemyModel enemy, LevelModel levelModel, double deltaTime) {

        float sweepDistance = (float) Math.abs(enemy.getXVelocity() * deltaTime);
        float probeX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth())
            : (float) (enemy.getX() - sweepDistance);
        float probeY = (float) enemy.getY() + 2.0f;
        float probeHeight = (float) enemy.getHeight() - 4.0f;

        Rectangle wallProbe = new Rectangle(probeX, probeY, sweepDistance, probeHeight);

        for (Rectangle rect : levelModel.getGroundBounds()) {
            if (wallProbe.overlaps(rect)) {
                return true;
            }
        }
        return false;
    }
}
