package Main.core.view;

import Main.core.model.KnightModel;
import Main.core.model.knightState.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KnightView {

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

    private float stateTime;
    private KnightState previousState;

    public KnightView(KnightModel knightModel, TextureAtlas textureAtlas) {

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
    }

    public void render(SpriteBatch spriteBatch, float deltaTime) {

        updateStateTime(deltaTime);
        TextureRegion currentFrame = determineCurrentFrame();
        adjustFrameFacingDirection(currentFrame);
        drawFrameWithDamageEffect(spriteBatch, currentFrame);
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

            if (knightModel.isDown()) {

                return attackDownAnimation.getKeyFrame(stateTime);
            }
            else {

                return attackSideAnimation.getKeyFrame(stateTime);
            }
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

        float regionWidth = currentFrame.getRegionWidth();
        float regionHeight = currentFrame.getRegionHeight();

        float visualScale = 1.2f;
        float renderHeight = hitboxHeight * visualScale;
        float renderWidth = renderHeight * (regionWidth / regionHeight);

        float renderX = hitboxX - (renderWidth - hitboxWidth) / 2.0f;
        float renderY = hitboxY;

        if (knightModel.isInvincible()) {

            applyBlinkingDamageEffect(spriteBatch);
        }

        spriteBatch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);

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
}
