package Main.core.view.renderer;

import Main.core.model.enemy.boss.FalseKnightModel;
import Main.core.model.enemy.boss.state.*;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.EnemyTurnState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FalseKnightRenderer {

    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> turnAnim;

    private final Animation<TextureRegion> attackAnticAnim;
    private final Animation<TextureRegion> attackAnim;
    private final Animation<TextureRegion> attackRecoverAnim;

    private final Animation<TextureRegion> runAnticAnim;
    private final Animation<TextureRegion> runAnim;

    private final Animation<TextureRegion> jumpAnticAnim;
    private final Animation<TextureRegion> jumpAnim;
    private final Animation<TextureRegion> landAnim;

    private final Animation<TextureRegion> jumpAttackAnim;
    private final Animation<TextureRegion> jumpAttackHitAnim;

    private final Animation<TextureRegion> stunRecoverAnim;
    private final Animation<TextureRegion> stunHitAnim;
    private final Animation<TextureRegion> deathFallAnim;
    private final Animation<TextureRegion> deathLandAnim;

    private final Animation<TextureRegion> shockwaveAnim;

    private float stateTime = 0.0f;
    private Class<? extends EnemyState> previousStateClass = null;

    public FalseKnightRenderer(TextureAtlas atlas) {

        idleAnim = new Animation<>(0.15f, atlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        turnAnim = new Animation<>(0.1f, atlas.findRegions("Turn"), Animation.PlayMode.NORMAL);

        attackAnticAnim = new Animation<>(0.1f, atlas.findRegions("Attack Antic"), Animation.PlayMode.NORMAL);
        attackAnim = new Animation<>(0.05f, atlas.findRegions("Attack"), Animation.PlayMode.NORMAL);
        attackRecoverAnim = new Animation<>(0.1f, atlas.findRegions("Attack Recover"), Animation.PlayMode.NORMAL);

        runAnticAnim = new Animation<>(0.1f, atlas.findRegions("Run Antic"), Animation.PlayMode.NORMAL);
        runAnim = new Animation<>(0.08f, atlas.findRegions("Run"), Animation.PlayMode.LOOP);

        jumpAnticAnim = new Animation<>(0.1f, atlas.findRegions("Jump Antic"), Animation.PlayMode.NORMAL);
        jumpAnim = new Animation<>(0.1f, atlas.findRegions("Jump"), Animation.PlayMode.LOOP);
        landAnim = new Animation<>(0.1f, atlas.findRegions("Land"), Animation.PlayMode.NORMAL);

        jumpAttackAnim = new Animation<>(0.1f, atlas.findRegions("Jump Attack"), Animation.PlayMode.NORMAL);
        jumpAttackHitAnim = new Animation<>(0.1f, atlas.findRegions("Jump Attack Hit"), Animation.PlayMode.NORMAL);

        stunRecoverAnim = new Animation<>(0.15f, atlas.findRegions("Stun Recover"), Animation.PlayMode.NORMAL);
        stunHitAnim = new Animation<>(0.07f, atlas.findRegions("Stun Hit"), Animation.PlayMode.NORMAL);

        deathFallAnim = new Animation<>(0.1f, atlas.findRegions("DeathFall"), Animation.PlayMode.NORMAL);
        deathLandAnim = new Animation<>(0.1f, atlas.findRegions("DeathLand"), Animation.PlayMode.NORMAL);

        shockwaveAnim = new Animation<>(0.08f, atlas.findRegions("Shockwave Spurt"), Animation.PlayMode.LOOP);
    }

    public void render(SpriteBatch batch, FalseKnightModel boss, float deltaTime) {

        EnemyState currentState = boss.getCurrentState();

        if (currentState == null && previousStateClass != null) {

            stateTime = 0.0f;
            previousStateClass = null;
        }
        else if (currentState != null && currentState.getClass() != previousStateClass) {

            stateTime = 0.0f;
            previousStateClass = currentState.getClass();
        }

        stateTime += deltaTime;
        TextureRegion currentFrame = determineFrame(boss, currentState);

        if (currentFrame != null) {

            boolean shouldFaceRight = boss.isFacingRight();
            if (currentFrame.isFlipX() != shouldFaceRight) {

                currentFrame.flip(true, false);
            }

            float scale = 1.5f;
            float renderWidth = currentFrame.getRegionWidth() * scale;
            float renderHeight = currentFrame.getRegionHeight() * scale;

            float renderX = (float) boss.getX() - (renderWidth - (float) boss.getWidth()) / 2.0f;
            float renderY = (float) boss.getY();

            batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
        }

        for (Main.core.model.enemy.boss.BossShockWave wave : boss.getActiveShockWaves()) {

            if (wave.isActive()) {
                TextureRegion waveFrame = shockwaveAnim.getKeyFrame(stateTime);

                boolean waveGoingRight = wave.getBounds().x > boss.getX();
                if (waveFrame.isFlipX() != waveGoingRight) {
                    waveFrame.flip(true, false);
                }

                float scaleX = wave.getBounds().width / waveFrame.getRegionWidth();
                float scaleY = wave.getBounds().height / waveFrame.getRegionHeight();

                batch.draw(
                    waveFrame,
                    wave.getBounds().x - (waveFrame.getRegionWidth() * 0.25f),
                    wave.getBounds().y,
                    waveFrame.getRegionWidth() * 1.5f,
                    waveFrame.getRegionHeight() * 1.5f
                );
            }
        }
    }

    private TextureRegion determineFrame(FalseKnightModel boss, EnemyState state) {

        if (boss.isDead()) {

            if (!deathFallAnim.isAnimationFinished(stateTime)) {

                return deathFallAnim.getKeyFrame(stateTime);
            } else {

                float landStateTime = stateTime - deathFallAnim.getAnimationDuration();
                return deathLandAnim.getKeyFrame(landStateTime);
            }
        }

        if (state == null) {

            return idleAnim.getKeyFrame(stateTime);
        }

        if (state instanceof EnemyTurnState) {

            return turnAnim.getKeyFrame(stateTime);
        }

        if (state instanceof MaceSlamState) {

            if (stateTime < 0.6f)
                return attackAnticAnim.getKeyFrame(stateTime);
            if (stateTime < 0.8f)
                return attackAnim.getKeyFrame(stateTime - 0.6f);

            return attackRecoverAnim.getKeyFrame(stateTime - 0.8f);
        }

        if (state instanceof ChargeState) {

            if (stateTime < 0.4f)
                return runAnticAnim.getKeyFrame(stateTime);

            return runAnim.getKeyFrame(stateTime - 0.4f);
        }

        if (state instanceof OffensiveLeapState || state instanceof DefensiveLeapState) {

            if (stateTime < 0.2f)
                return jumpAnticAnim.getKeyFrame(stateTime);
            if (!boss.isGrounded())
                return jumpAnim.getKeyFrame(stateTime - 0.2f);

            return landAnim.getKeyFrame(stateTime);
        }

        if (state instanceof ShockWaveLandingState) {

            if (!boss.isGrounded())
                return jumpAttackAnim.getKeyFrame(stateTime);

            return jumpAttackHitAnim.getKeyFrame(stateTime);
        }

        if (state instanceof StunState) {

            if (!deathFallAnim.isAnimationFinished(stateTime)) {

                return deathFallAnim.getKeyFrame(stateTime);
            }

            float landStateTime = stateTime - deathFallAnim.getAnimationDuration();
            if (!deathLandAnim.isAnimationFinished(landStateTime)) {

                return deathLandAnim.getKeyFrame(landStateTime);
            }

            if (stateTime - deathFallAnim.getAnimationDuration() - deathLandAnim.getAnimationDuration() > 3.0f) {

                return stunRecoverAnim.getKeyFrame(stateTime
                    - deathFallAnim.getAnimationDuration() - deathLandAnim.getAnimationDuration() - 3.0f);
            }

            if (boss.isHurt()) {

                float hitTime = 0.2f - (float) boss.getHurtTimer();
                return stunHitAnim.getKeyFrame(hitTime);
            }

            return deathLandAnim.getKeyFrame(landStateTime);
        }

        return idleAnim.getKeyFrame(stateTime);
    }
}
