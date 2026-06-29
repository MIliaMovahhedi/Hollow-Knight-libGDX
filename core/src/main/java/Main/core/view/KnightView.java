package Main.core.view;

import Main.core.model.KnightModel;
import Main.core.model.knightState.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KnightView {

    private final KnightModel knightModel;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> dashAnimation;

    private float stateTime;

    public KnightView(KnightModel knightModel, TextureAtlas textureAtlas) {

        this.knightModel = knightModel;
        this.stateTime = 0.0f;
        this.idleAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        this.runAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Run"), Animation.PlayMode.LOOP);
        this.jumpAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Double Jump"), Animation.PlayMode.NORMAL);
        this.fallAnimation = new Animation<>(0.1f, textureAtlas.findRegions("Fall"), Animation.PlayMode.LOOP);
        this.dashAnimation = new Animation<>(0.05f, textureAtlas.findRegions("Dash"), Animation.PlayMode.NORMAL);
    }

    public void render(SpriteBatch spriteBatch, float deltaTime) {

        this.stateTime += deltaTime;
        TextureRegion currentFrame = determineCurrentFrame();
        adjustFrameFacingDirection(currentFrame);
        drawFrame(spriteBatch, currentFrame);
    }

    private TextureRegion determineCurrentFrame() {

        KnightState currentState = knightModel.getKnightState();

        if (currentState instanceof DashState) {

            return dashAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof JumpState) {

            return jumpAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof FallState) {

            return fallAnimation.getKeyFrame(stateTime);
        }
        else if (currentState instanceof RunState) {

            return runAnimation.getKeyFrame(stateTime);
        }

        return idleAnimation.getKeyFrame(stateTime);
    }

    private void adjustFrameFacingDirection(TextureRegion currentFrame) {

        boolean shouldFaceLeft = !knightModel.isFacingRight();

        if (currentFrame.isFlipX() == shouldFaceLeft) {

            currentFrame.flip(true, false);
        }
    }

    private void drawFrame(SpriteBatch spriteBatch, TextureRegion currentFrame) {


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

        spriteBatch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
    }
}
