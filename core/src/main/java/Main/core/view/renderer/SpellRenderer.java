package Main.core.view.renderer;

import Main.core.model.knight.KnightModel;
import Main.core.model.knight.spell.HowlingWraiths;
import Main.core.model.knight.spell.Spell;
import Main.core.model.knight.spell.VengefulSpirit;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class SpellRenderer {

    private final KnightModel knightModel;

    private final Animation<TextureRegion> vengefulSpiritAnim;
    private final Animation<TextureRegion> howlingWraithsAnim;
    private final Animation<TextureRegion> blastAnim;

    private float vengefulSpiritStateTime;

    public SpellRenderer(KnightModel knightModel, TextureAtlas atlas) {

        this.knightModel = knightModel;
        this.vengefulSpiritStateTime = 0.0f;

        this.vengefulSpiritAnim = new Animation<>(0.08f, atlas.findRegions("Vengeful Spirit"), Animation.PlayMode.LOOP);
        this.howlingWraithsAnim = new Animation<>(0.07f, atlas.findRegions("SoulScream"), Animation.PlayMode.NORMAL);
        this.blastAnim = new Animation<>(0.1f, atlas.findRegions("Blast"), Animation.PlayMode.NORMAL);
    }

    public void render(SpriteBatch batch, float deltaTime) {
        List<Spell> activeSpells = knightModel.getActiveSpells();
        if (activeSpells == null || activeSpells.isEmpty()) {
            return;
        }

        vengefulSpiritStateTime += deltaTime;
        for (Spell spell : activeSpells) {
            if (spell instanceof VengefulSpirit) {
                drawVengefulSpirit(batch, (VengefulSpirit) spell);
            }
            else if (spell instanceof HowlingWraiths) {
                drawHowlingWraiths(batch, (HowlingWraiths) spell);
            }
        }
    }

    private void drawVengefulSpirit(SpriteBatch batch, VengefulSpirit spirit) {
        boolean shouldFaceLeft = !spirit.isFacingRight();

        if (!blastAnim.isAnimationFinished((float) spirit.getBlastTimer())) {
            TextureRegion blastFrame = blastAnim.getKeyFrame((float) spirit.getBlastTimer());
            if (blastFrame.isFlipX() != shouldFaceLeft) {
                blastFrame.flip(true, false);
            }
            float blastWidth = spirit.getWidth() * 1.5f;
            float blastHeight = spirit.getHeight() * 1.5f;
            float blastX = (float) spirit.getStartX() - (blastWidth - spirit.getWidth()) / 2.0f;
            float blastY = (float) spirit.getStartY() - (blastHeight - spirit.getHeight()) / 2.0f;
            batch.draw(blastFrame, blastX, blastY, blastWidth, blastHeight);
        }

        TextureRegion currentFrame = vengefulSpiritAnim.getKeyFrame(vengefulSpiritStateTime);
        if (currentFrame.isFlipX() != shouldFaceLeft) {
            currentFrame.flip(true, false);
        }
        float renderWidth = spirit.getWidth() * 1.5f;
        float renderHeight = spirit.getHeight() * 1.5f;
        float renderX = (float) spirit.getX() - (renderWidth - spirit.getWidth()) / 2.0f;
        float renderY = (float) spirit.getY() - (renderHeight - spirit.getHeight()) / 2.0f;
        batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
    }

    private void drawHowlingWraiths(SpriteBatch batch, HowlingWraiths wraiths) {

        TextureRegion currentFrame = howlingWraithsAnim.getKeyFrame((float) wraiths.getTimer());
        float renderWidth = wraiths.getWidth() * 2.0f;
        float renderHeight = wraiths.getHeight() * 2.0f;
        float renderX = (float) wraiths.getX() - (renderWidth - wraiths.getWidth()) / 2.0f;
        float renderY = (float) wraiths.getY();
        batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
    }
}
