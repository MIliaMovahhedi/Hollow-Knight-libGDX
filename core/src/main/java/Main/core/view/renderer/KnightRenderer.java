package Main.core.view.renderer;

import Main.core.model.knight.KnightModel;
import Main.core.model.knight.knightState.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KnightRenderer {

    private final KnightModel knightModel;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> idleHurtAnimation;

    private final Animation<TextureRegion> runAnimation;

    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> doubleJumpAnimation;
    private final Animation<TextureRegion> fallAnimation;

    private final Animation<TextureRegion> dashAnimation;

    private final Animation<TextureRegion> attackSideAnimation;
    private final Animation<TextureRegion> attackDownAnimation;

    private final Animation<TextureRegion> castForwardAnimation;
    private final Animation<TextureRegion> castUpAnimation;

    private final Animation<TextureRegion> focusStartAnimation;
    private final Animation<TextureRegion> focusGetAnimation;
    private final Animation<TextureRegion> focusEndAnimation;
    private final Animation<TextureRegion> focusAnimation;

    private final Animation<TextureRegion> frontSlashVFXAnimation;
    private final Animation<TextureRegion> downSlashVFXAnimation;
    private final Animation<TextureRegion> dashVFXAnimation;

    private float stateTime;
    private KnightState previousState;

    private int previousHealthTracker;
    private float focusGetEffectTimer;

    public KnightRenderer(KnightModel knightModel, TextureAtlas textureAtlas) {

        this.knightModel = knightModel;
        this.stateTime = 0.0f;
        this.idleAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        this.idleHurtAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Idle Hurt"), Animation.PlayMode.NORMAL);
        this.runAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Run"), Animation.PlayMode.LOOP);
        this.jumpAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Jump"), Animation.PlayMode.NORMAL);
        this.doubleJumpAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Double Jump"), Animation.PlayMode.NORMAL);
        this.fallAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Fall"), Animation.PlayMode.LOOP);
        this.dashAnimation = new Animation<>(0.05f, textureAtlas.findRegions("Dash"), Animation.PlayMode.NORMAL);
        this.attackSideAnimation = new Animation<>(0.05f, textureAtlas.findRegions("SlashAlt"), Animation.PlayMode.NORMAL);
        this.attackDownAnimation = new Animation<>(0.05f, textureAtlas.findRegions("DownSlash"), Animation.PlayMode.NORMAL);
        this.castForwardAnimation = new Animation<>(0.08f, textureAtlas.findRegions("Fireball Cast"), Animation.PlayMode.NORMAL);
        this.castUpAnimation = new Animation<>(0.08f, textureAtlas.findRegions("Scream"), Animation.PlayMode.LOOP);

        this.focusStartAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Focus Start"), Animation.PlayMode.NORMAL);
        this.focusGetAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Focus Get"), Animation.PlayMode.NORMAL);
        this.focusEndAnimation = new Animation<>(0.08f, textureAtlas.findRegions("Focus End"), Animation.PlayMode.NORMAL);
        this.focusAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Focus"), Animation.PlayMode.LOOP);

        this.frontSlashVFXAnimation = new Animation<>(0.02f, textureAtlas.findRegions("SlashEffectAlt"), Animation.PlayMode.NORMAL);
        this.downSlashVFXAnimation = new Animation<>(0.02f, textureAtlas.findRegions("DownSlashEffect"), Animation.PlayMode.NORMAL);
        this.dashVFXAnimation = new Animation<>(0.02f, textureAtlas.findRegions("Dash Effect"), Animation.PlayMode.NORMAL);

        this.previousHealthTracker = (int) knightModel.getCurrentHealth();
        this.focusGetEffectTimer = -1.0f;
    }

    public void render(SpriteBatch spriteBatch, float deltaTime) {

        updateStateTime(deltaTime);
        trackHealingEffect(deltaTime);

        TextureRegion currentFrame = determineCurrentFrame();
        adjustFrameFacingDirection(currentFrame);
        drawFrameWithDamageEffect(spriteBatch, currentFrame);
        drawVisualEffects(spriteBatch);
    }

    private void updateStateTime(float deltaTime) {

        KnightState currentState = knightModel.getKnightState();
        if (currentState != previousState) {

            stateTime = 0.0f;
            previousState = currentState;
        }
        stateTime += deltaTime;
    }
    private TextureRegion determineCurrentFrame() {

        KnightState currentState = knightModel.getKnightState();

        if (currentState instanceof AttackState) {

            float speedMultiplier = (float) (knightModel.getBaseAttackDuration() / knightModel.getAttackDuration());

            if (knightModel.isDown()) {

                return attackDownAnimation.getKeyFrame(stateTime * speedMultiplier);
            }
            else {

                return attackSideAnimation.getKeyFrame(stateTime * speedMultiplier);
            }
        }
        else if (currentState instanceof CastState castState) {

            if (castState.isUpwardCast()) {

                return castUpAnimation.getKeyFrame(stateTime);
            }
            else {

                return castForwardAnimation.getKeyFrame(stateTime);
            }
        }
        else if (currentState instanceof FocusState) {

            if (!focusStartAnimation.isAnimationFinished(stateTime)) {

                return focusStartAnimation.getKeyFrame(stateTime);
            }
            else if (focusGetEffectTimer >= 0.0f) {

                return focusGetAnimation.getKeyFrame(focusGetEffectTimer);
            }
            else {

                float loopTime = stateTime - focusStartAnimation.getAnimationDuration();
                return focusAnimation.getKeyFrame(loopTime);
            }
        }
        else if (currentState instanceof FocusEndState) {

            return focusEndAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof DashState) {

            return dashAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof JumpState) {

            if (((JumpState) currentState).isOnDoubleJump()) {

                return doubleJumpAnimation.getKeyFrame(stateTime);
            }

            return jumpAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof FallState) {

            return fallAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof RunState) {

            return runAnimation.getKeyFrame(stateTime);
        }

        if (knightModel.isOnHurt())
            return idleHurtAnimation.getKeyFrame(stateTime);

        return idleAnimation.getKeyFrame(stateTime);
    }
    private void adjustFrameFacingDirection(TextureRegion currentFrame) {

        boolean shouldFaceLeft = !knightModel.isFacingRight();

        if (currentFrame.isFlipX() == shouldFaceLeft) {

            currentFrame.flip(true, false);
        }
    }
    private void drawFrameWithDamageEffect(SpriteBatch spriteBatch, TextureRegion currentFrame) {

        float hitboxX = (float) knightModel.getX();
        float hitboxY = (float) knightModel.getY();
        float hitboxWidth = (float) knightModel.getWidth();
        float hitboxHeight = (float) knightModel.getHeight();

        float visualScale = 1f;
        float regionWidth = currentFrame.getRegionWidth() * visualScale;
        float regionHeight = currentFrame.getRegionHeight() * visualScale;

        float renderX = hitboxX - (regionWidth - hitboxWidth) / 2.0f;
        float renderY = hitboxY;

        if (knightModel.isInvincible()) {

            applyBlinkingDamageEffect(spriteBatch);
        }

        spriteBatch.draw(currentFrame, renderX, renderY, regionWidth, regionHeight);

        spriteBatch.setColor(Color.WHITE);
    }
    private void applyBlinkingDamageEffect(SpriteBatch spriteBatch) {

        int blinkFrequency = (int) (stateTime * 15);
        if (blinkFrequency % 2 == 0) {

            spriteBatch.setColor(1.0f, 0.3f, 0.3f, 0.8f);
        }
        else {

            spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.4f);
        }
    }

    private void trackHealingEffect(float deltaTime) {

        int currentHealth = (int) knightModel.getCurrentHealth();

        if (currentHealth > previousHealthTracker) {

            focusGetEffectTimer = 0.0f;
        }
        previousHealthTracker = currentHealth;

        if (focusGetEffectTimer >= 0.0f) {

            focusGetEffectTimer += deltaTime;

            if (focusGetAnimation.isAnimationFinished(focusGetEffectTimer)) {

                focusGetEffectTimer = -1.0f;
            }
        }
    }

    private void drawVisualEffects(SpriteBatch batch) {

        KnightState currentState = knightModel.getKnightState();
        float x = (float) knightModel.getX();
        float y = (float) knightModel.getY();
        boolean facingRight = knightModel.isFacingRight();

        TextureRegion vfxFrame;

        if (currentState instanceof AttackState) {

            AttackState attackState = (AttackState) currentState;

            if (attackState.isDownAttack()) {

                vfxFrame = downSlashVFXAnimation.getKeyFrame(stateTime);
                float padding = (vfxFrame.getRegionWidth() -  (float) knightModel.getWidth()) / 2.0f;

                if (vfxFrame.isFlipX() != facingRight) {

                    vfxFrame.flip(true, false);
                }
                batch.draw(vfxFrame, x - padding, y - (float) vfxFrame.getRegionHeight() / 2);
            }

            else {

                vfxFrame = frontSlashVFXAnimation.getKeyFrame(stateTime);

                if (vfxFrame.isFlipX() != facingRight) {

                    vfxFrame.flip(true, false);
                }

                float vfxX = facingRight ? x - 100 : x + 100 - vfxFrame.getRegionWidth() + (float) knightModel.getWidth();
                batch.draw(vfxFrame, vfxX, y);
            }
        }
        else if (currentState instanceof DashState) {

            vfxFrame = dashVFXAnimation.getKeyFrame(stateTime);

            if (vfxFrame.isFlipX() == facingRight) {

                vfxFrame.flip(true, false);
            }

            float vfxX = facingRight ? x - vfxFrame.getRegionWidth() + 20.0f : x + (float) knightModel.getWidth() - 20.0f;
            batch.draw(vfxFrame, vfxX, y - vfxFrame.getRegionHeight() / 4.0f);
        }
    }
}
