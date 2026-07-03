package Main.core.model.enemy.state.crystalGuardianState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.CrystalGuardianModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class GuardianShootState implements EnemyState {

    private final double remainingEnrageTime;
    private double windupTimer = 0.0;
    private double laserTimer = 0.0;
    private final double WINDUP_DURATION = 0.6;
    private final double LASER_DURATION = 0.4;
    private final float INFINITE_RANGE = 10000.0f;
    private final float LASER_HEIGHT = 30.0f;
    private boolean hasFired = false;

    public GuardianShootState(double remainingEnrageTime) {

        this.remainingEnrageTime = remainingEnrageTime;
    }

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        windupTimer = 0.0;
        laserTimer = 0.0;
        hasFired = false;
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        windupTimer += deltaTime;

        if (windupTimer >= WINDUP_DURATION) {

            if (!hasFired) {

                fireLaser(enemy);
                hasFired = true;
            }

            laserTimer += deltaTime;

            if (laserTimer >= LASER_DURATION) {

                if (remainingEnrageTime > 0) {

                    return new GuardianEnragedState(remainingEnrageTime);
                }
                else {

                    return new GuardianIdleState();
                }
            }
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        if (enemy instanceof CrystalGuardianModel) {

            ((CrystalGuardianModel) enemy).setActiveLaserBounds(null);
        }
    }

    private void fireLaser(EnemyModel enemy) {

        float laserX = enemy.isFacingRight()
            ? (float) (enemy.getX() + enemy.getWidth())
            : (float) (enemy.getX() - INFINITE_RANGE);

        float laserY = (float) enemy.getY() + ((float) enemy.getHeight() / 2.0f) - (LASER_HEIGHT / 2.0f);

        Rectangle laserBounds = new Rectangle(laserX, laserY, INFINITE_RANGE, LASER_HEIGHT);

        if (enemy instanceof CrystalGuardianModel) {

            ((CrystalGuardianModel) enemy).setActiveLaserBounds(laserBounds);
        }
    }
}
