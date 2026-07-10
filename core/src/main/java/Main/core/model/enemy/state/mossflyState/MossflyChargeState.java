package Main.core.model.enemy.state.mossflyState;

import Main.core.controller.manager.AudioManager;
import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class MossflyChargeState implements EnemyState {

    private final double targetX;
    private final double targetY;
    private final double CHARGE_SPEED = 300.0;
    private final double ARRIVAL_THRESHOLD = 5.0;

    private long chargeSoundId;
    private final float MAX_HEARING_DISTANCE = 800.0f;

    public MossflyChargeState(double targetX, double targetY) {

        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void enter(EnemyModel enemy) {

        chargeSoundId = AudioManager.getInstance().loopSpatialSfx(
            AudioManager.getInstance().mossflyFlySound,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );

        double deltaX = targetX - enemy.getX();
        double deltaY = targetY - enemy.getY();

        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

        if (distance > 0) {

            enemy.setXVelocity((deltaX / distance) * CHARGE_SPEED);
            enemy.setYVelocity((deltaY / distance) * CHARGE_SPEED);

            enemy.setFacingRight(deltaX > 0);
        }
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        AudioManager.getInstance().updateLoopVolume(
            AudioManager.getInstance().crawlidCrawlSound,
            chargeSoundId,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );

        double currentDeltaX = targetX - enemy.getX();
        double currentDeltaY = targetY - enemy.getY();
        double distanceToTarget = Math.sqrt((currentDeltaX * currentDeltaX) + (currentDeltaY * currentDeltaY));

        if (distanceToTarget <= ARRIVAL_THRESHOLD ||
            isHittingWall(enemy, levelModel) ||
            enemy.isGrounded() ||
            (enemy.getXVelocity() == 0 && currentDeltaX != 0)) {

            return new MossflyHoverState();
        }
        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
        enemy.setYVelocity(0);

        AudioManager.getInstance().stopSfx(AudioManager.getInstance().mossflyFlySound);
    }
}
