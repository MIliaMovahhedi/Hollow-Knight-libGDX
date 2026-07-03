package Main.core.model.enemy.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public interface EnemyState {

    void enter(EnemyModel enemy);
    EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime);
    void exit(EnemyModel enemy);

    default boolean isHittingWall(EnemyModel enemy, LevelModel levelModel) {

        float PROBE_SIZE = 5.0f;

        float probeX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth())
            : (float) (enemy.getX() - PROBE_SIZE);

        float probeY = (float) enemy.getY() + 2.0f;
        float probeHeight = (float) enemy.getHeight() - 4.0f;

        Rectangle wallProbe = new Rectangle(probeX, probeY, PROBE_SIZE, probeHeight);

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (wallProbe.overlaps(rect)) {

                return true;
            }
        }
        return false;
    }

    default boolean isAtCliffEdge(EnemyModel enemy, LevelModel levelModel) {

        float EDGE_OFFSET = 5.0f;
        float PROBE_SIZE = 5.0f;

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
