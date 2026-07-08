package Main.core.model.enemy.state.crystalGuardianState;

import Main.core.controller.manager.AudioManager;
import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.EnemyTurnState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class GuardianEnragedState implements EnemyState {

    private final double CHARGE_SPEED = 300.0;
    private final double FAST_COOLDOWN = 1.5;
    private final float INFINITE_RANGE = 10000.0f;
    private double enragedTimer;
    private double cooldownTimer = 0.0;

    private long runSoundId;
    private final float MAX_HEARING_DISTANCE = 800.0f;

    public GuardianEnragedState(double remainingEnrageTime) {

        this.enragedTimer = remainingEnrageTime;
    }

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        cooldownTimer = 0.0;

        runSoundId = AudioManager.getInstance().loopSpatialSfx(
            AudioManager.getInstance().guardianRunSound,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        enragedTimer -= deltaTime;
        cooldownTimer += deltaTime;

        AudioManager.getInstance().updateLoopVolume(
            AudioManager.getInstance().crawlidCrawlSound,
            runSoundId,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );

        if (enragedTimer <= 0) {

            return new GuardianIdleState();
        }

        boolean targetIsRight = knightModel.getX() > enemy.getX();
        if (targetIsRight != enemy.isFacingRight()) {

            return new EnemyTurnState(new GuardianEnragedState(enragedTimer));
        }

        enemy.setXVelocity(enemy.isFacingRight() ? CHARGE_SPEED : -CHARGE_SPEED);

        if (cooldownTimer >= FAST_COOLDOWN && seesKnight(enemy, knightModel)) {

            return new GuardianShootState(enragedTimer);
        }

        if (isHittingWall(enemy, levelModel) || isAtCliffEdge(enemy, levelModel)) {

            return new GuardianIdleState();
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        AudioManager.getInstance().stopSfx(AudioManager.getInstance().guardianRunSound);
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
}
