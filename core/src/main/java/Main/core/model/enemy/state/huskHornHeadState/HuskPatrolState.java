package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class HuskPatrolState implements EnemyState {

    private final double PATROL_SPEED = 60.0;
    private final double PATROL_DURATION = 3.0;
    private final float VISION_RANGE = 250.0f;
    private double patrolTimer = 0.0;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(enemy.isFacingRight() ? PATROL_SPEED : -PATROL_SPEED);
        patrolTimer = 0.0;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        patrolTimer += deltaTime;

        if (seesKnight(enemy, knightModel)) {
            return new HuskChargeState();
        }

        if (patrolTimer >= PATROL_DURATION) {
            return new HuskRestState();
        }

        enemy.setXVelocity(enemy.isFacingRight() ? PATROL_SPEED : -PATROL_SPEED);

        if (isHittingWall(enemy, levelModel, deltaTime) || isAtCliffEdge(enemy, levelModel)) {
            enemy.setFacingRight(!enemy.isFacingRight());
            enemy.setXVelocity(enemy.isFacingRight() ? PATROL_SPEED : -PATROL_SPEED);
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

    private boolean isAtCliffEdge(EnemyModel enemy, LevelModel levelModel) {

        float EDGE_OFFSET = 5.0f;
        float PROBE_SIZE = 2.0f;

        float probeX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth() + EDGE_OFFSET)
            : (float) (enemy.getX() - EDGE_OFFSET - PROBE_SIZE);
        float probeY = (float) (enemy.getY() - PROBE_SIZE);

        Rectangle cliffProbe = new Rectangle(probeX, probeY, PROBE_SIZE, PROBE_SIZE);

        for (Rectangle rect : levelModel.getGroundBounds()) {
            if (cliffProbe.overlaps(rect)) {
                return false;
            }
        }
        return true;
    }
}
