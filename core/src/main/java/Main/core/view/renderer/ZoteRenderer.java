package Main.core.view.renderer;

import Main.core.model.zote.ZoteModel;
import Main.core.model.zote.state.ZoteState;
import Main.core.model.zote.state.ZoteTalkingState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZoteRenderer {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> talkingAnimation;
    private float stateTime;
    private Class<? extends ZoteState> previousStateClass;

    public ZoteRenderer(TextureAtlas atlas) {

        idleAnimation = new Animation<>(0.15f, atlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        talkingAnimation = new Animation<>(0.12f, atlas.findRegions("Talk"), Animation.PlayMode.LOOP);
        stateTime = 0.0f;
    }

    public void render(SpriteBatch batch, ZoteModel zote, float deltaTime) {

        Class<? extends ZoteState> currentStateClass = zote.getCurrentState().getClass();

        if (currentStateClass != previousStateClass) {

            stateTime = 0.0f;
            previousStateClass = currentStateClass;
        }

        stateTime += deltaTime;
        TextureRegion currentFrame;

        if (currentStateClass == ZoteTalkingState.class) {

            currentFrame = talkingAnimation.getKeyFrame(stateTime);
        }
        else {

            currentFrame = idleAnimation.getKeyFrame(stateTime);
        }

        if (currentFrame != null) {

            float scale = 1f;
            float renderWidth = currentFrame.getRegionWidth() * scale;
            float renderHeight = currentFrame.getRegionHeight() * scale;
            float renderX = zote.getX() - (renderWidth - zote.getWidth()) / 2.0f;
            float renderY = zote.getY();

            batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
        }
    }
}
