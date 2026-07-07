package Main.core.view.renderer;

import Main.core.model.zote.ZoteModel;
import Main.core.model.zote.state.ZoteIdleState;
import Main.core.model.zote.state.ZoteTalkingState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class DialogueRenderer {

    private BitmapFont font;
    private GlyphLayout layout;
    private String[] zoteDialogues;
    private String[] zotePrecepts;
    private float typewriterTimer;
    private int visibleCharacters;
    private float charDelay;
    private Texture backgroundTexture;

    public DialogueRenderer(BitmapFont font) {
        this.font = font;
        this.layout = new GlyphLayout();
        this.zoteDialogues = new String[]{
            "Curse this miserable kingdom...",
            "I am Zote the Mighty, a knight of great renown!",
            "Step aside, weakling, before I draw my nail Life Ender!"
        };
        this.zotePrecepts = new String[]{
            "Precept One: Always Win Your Battles.",
            "Precept Two: Never Let Them Laugh at You.",
            "Precept Three: Always Be Rested."
        };
        this.charDelay = 0.05f;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.85f));
        pixmap.fill();
        this.backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, ZoteModel zote, float delta, OrthographicCamera camera) {
        if (zote.isCanInteract() && zote.getCurrentState() instanceof ZoteIdleState) {
            font.getData().setScale(0.4f);
            font.draw(batch, "Press ENTER to Interact", zote.getX() - 20, zote.getY() + 130);
            font.getData().setScale(1f);
        }

        if (zote.getCurrentState() instanceof ZoteTalkingState) {
            String fullText = getDialogueText(zote);
            updateTypewriter(fullText, delta);
            String visibleText = fullText.substring(0, visibleCharacters);

            float boxWidth = camera.viewportWidth * 0.7f;
            float boxHeight = 120f;
            float boxX = camera.position.x - (boxWidth / 2);
            float boxY = camera.position.y - (camera.viewportHeight / 2) + 30f;

            batch.draw(backgroundTexture, boxX, boxY, boxWidth, boxHeight);

            font.getData().setScale(0.55f);
            layout.setText(font, visibleText, Color.WHITE, boxWidth - 40, Align.left, true);
            font.draw(batch, layout, boxX + 20, boxY + boxHeight - 20);
            font.getData().setScale(1f);
        } else {
            resetTypewriter();
        }
    }

    private String getDialogueText(ZoteModel zote) {
        if (zote.isPreceptMode()) {
            return zotePrecepts[zote.getDialogueIndex() % zotePrecepts.length];
        } else {
            return zoteDialogues[zote.getDialogueIndex() % zoteDialogues.length];
        }
    }

    private void updateTypewriter(String text, float delta) {
        if (visibleCharacters < text.length()) {
            typewriterTimer += delta;
            if (typewriterTimer >= charDelay) {
                visibleCharacters++;
                typewriterTimer = 0f;
            }
        }
    }

    private void resetTypewriter() {
        visibleCharacters = 0;
        typewriterTimer = 0f;
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
