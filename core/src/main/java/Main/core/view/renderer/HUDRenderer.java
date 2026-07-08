package Main.core.view.renderer;

import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class HUDRenderer {

    private enum MaskState {
        FULL, SHINING, BREAKING, EMPTY, REFILLING
    }

    private class MaskData {

        MaskState currentState = MaskState.FULL;
        float stateTime = 0.0f;
    }

    private final KnightModel knightModel;
    private final OrthographicCamera hudCamera;

    private final TextureRegion healthBarTexture;
    private final TextureRegion maskFull;
    private final TextureRegion maskEmpty;
    private final Animation<TextureRegion> maskShineAnim;
    private final Animation<TextureRegion> maskBreakAnim;
    private final Animation<TextureRegion> maskRefillAnim;

    private final TextureRegion soulOrbFull;

    private final List<MaskData> masks;
    private int previousHealth;
    private float displayedSoulFill = 0.0f;


    public HUDRenderer(KnightModel knightModel, TextureAtlas atlas) {

        this.knightModel = knightModel;
        this.hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.setToOrtho(false, 1920, 1080);
        this.hudCamera.update();

        this.healthBarTexture = atlas.findRegion("HealthBarStatic");
        this.maskFull = atlas.findRegion("FilledHealth");
        this.maskEmpty = atlas.findRegion("EmptyHealth");

        this.maskShineAnim = new Animation<>(0.1f, atlas.findRegions("FilledHealthShine"), Animation.PlayMode.NORMAL);
        this.maskBreakAnim = new Animation<>(0.05f, atlas.findRegions("BreakHealth"), Animation.PlayMode.NORMAL);
        this.maskRefillAnim = new Animation<>(0.1f, atlas.findRegions("HealthRefill"), Animation.PlayMode.NORMAL);

        this.soulOrbFull = atlas.findRegion("SoulOrb_Full");

        this.masks = new ArrayList<>();
        this.previousHealth = (int) knightModel.getMaxHealth();

        for (int i = 0; i < previousHealth; i++) {
            masks.add(new MaskData());
        }
    }

    public void render(SpriteBatch batch, float deltaTime) {

        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);

        syncHealthStates();

        float virtualHeight = 1080f;

        float hudBaseX = 30f;
        float hudBaseY = virtualHeight - 180f;

        batch.begin();

        batch.draw(healthBarTexture, hudBaseX, hudBaseY);
        drawSoulVessel(batch, hudBaseX, hudBaseY, deltaTime);

        float masksStartX = hudBaseX + 130f;
        float masksStartY = hudBaseY + 40f;

        drawMasks(batch, deltaTime, masksStartX, masksStartY);

        batch.end();
    }

    private void syncHealthStates() {

        int currentHealth = (int) knightModel.getCurrentHealth();

        if (currentHealth < previousHealth) {

            for (int i = currentHealth; i < previousHealth; i++) {

                if (i < masks.size()) {
                    masks.get(i).currentState = MaskState.BREAKING;
                    masks.get(i).stateTime = 0.0f;
                }
            }
        }
        else if (currentHealth > previousHealth) {

            for (int i = previousHealth; i < currentHealth; i++) {

                if (i < masks.size()) {
                    masks.get(i).currentState = MaskState.REFILLING;
                    masks.get(i).stateTime = 0.0f;
                }
            }
        }

        previousHealth = currentHealth;
    }

    private void drawMasks(SpriteBatch batch, float deltaTime,  float masksStartX, float masksStartY) {

        float spacing = 60f;
        float maskScale = 0.65f;

        for (int i = 0; i < masks.size(); i++) {

            MaskData mask = masks.get(i);
            mask.stateTime += deltaTime;
            TextureRegion regionToDraw = determineMaskFrame(mask);

            if (regionToDraw != null) {

                float renderWidth = regionToDraw.getRegionWidth() * maskScale;
                float renderHeight = regionToDraw.getRegionHeight() * maskScale;

                float drawX = masksStartX + (i * spacing);

                batch.draw(regionToDraw, drawX, masksStartY, renderWidth, renderHeight);
            }
        }
    }
    private TextureRegion determineMaskFrame(MaskData mask) {

        return switch (mask.currentState) {

            case BREAKING -> {

                if (maskBreakAnim.isAnimationFinished(mask.stateTime)) {

                    mask.currentState = MaskState.EMPTY;
                    yield maskEmpty;
                }
                yield maskBreakAnim.getKeyFrame(mask.stateTime);
            }
            case REFILLING -> {

                if (maskRefillAnim.isAnimationFinished(mask.stateTime)) {

                    mask.currentState = MaskState.SHINING;
                    mask.stateTime = 0.0f;
                    yield maskShineAnim.getKeyFrame(mask.stateTime);
                }

                yield maskRefillAnim.getKeyFrame(mask.stateTime);
            }

            case SHINING -> maskShineAnim.getKeyFrame(mask.stateTime);
            case EMPTY -> maskEmpty;
            default -> maskFull;
        };
    }

    private void drawSoulVessel(SpriteBatch batch, float baseDrawX, float baseDrawY, float deltaTime) {

        float vesselX = baseDrawX;
        float vesselY = baseDrawY;

        float targetFillPercentage = (float) knightModel.getCurrentSoul() / 99.0f;
        targetFillPercentage = Math.max(0.0f, min(1.0f, targetFillPercentage));

        float lerpSpeed = 5.0f;
        displayedSoulFill += (float) ((targetFillPercentage - displayedSoulFill) * min(1.0f, lerpSpeed * deltaTime));

        float fillPercentage = (float) knightModel.getCurrentSoul() / 99.0f;
        fillPercentage = Math.max(0.0f, min(1.0f, fillPercentage));

        if (fillPercentage > 0) {

            int totalHeight = soulOrbFull.getRegionHeight();
            int fillHeight = (int) (totalHeight * fillPercentage);

            int startY = totalHeight - fillHeight;

            TextureRegion croppedLiquid = new TextureRegion(
                soulOrbFull,
                0,
                startY,
                soulOrbFull.getRegionWidth(),
                fillHeight
            );

            batch.draw(croppedLiquid, vesselX, vesselY);
        }
    }
}
