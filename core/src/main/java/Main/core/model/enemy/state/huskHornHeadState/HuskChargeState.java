package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.controller.manager.AudioManager;
import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class HuskChargeState implements EnemyState {

    private double timer = 0.0;
    private final double ANTICIPATE_DURATION = 0.4;
    private final double CHARGE_DURATION = 1.0;
    private final double CHARGE_SPEED = 550.0;
    private boolean isCharging = false;

    private long runSoundId;
    private final float MAX_HEARING_DISTANCE = 800.0f;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        timer = 0.0;
        isCharging = false;

        runSoundId = AudioManager.getInstance().loopSpatialSfx(
            AudioManager.getInstance().huskAttackSound,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {
        timer += deltaTime;

        AudioManager.getInstance().updateLoopVolume(
            AudioManager.getInstance().crawlidCrawlSound,
            runSoundId,
            enemy.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );

        if (!isCharging) {

            if (timer >= ANTICIPATE_DURATION) {
                isCharging = true;
                timer = 0.0;

                enemy.activateDynamicHitbox(enemy.getWidth() * 1.6, enemy.getHeight() * 0.65);
            }
        }
        else {
            enemy.setXVelocity(enemy.isFacingRight() ? CHARGE_SPEED : -CHARGE_SPEED);

            if (timer >= CHARGE_DURATION || isHittingWall(enemy, levelModel) || isAtCliffEdge(enemy, levelModel)) {
                return new HuskCooldownState();
            }
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
        AudioManager.getInstance().stopSfx(AudioManager.getInstance().huskAttackSound);

        enemy.deactivateDynamicHitbox();
    }
}
