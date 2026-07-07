package Main.core.model.zote;

import Main.core.model.knight.KnightModel;
import Main.core.model.zote.state.ZoteIdleState;
import Main.core.model.zote.state.ZoteState;
import com.badlogic.gdx.math.Rectangle;

public class ZoteModel {

    private float coordinateX;
    private float coordinateY;
    private float width;
    private float height;

    private float interactionRadius;
    private Rectangle interactionBounds;

    private ZoteState currentState;

    private boolean canInteract;
    private int dialogueIndex;
    private boolean isPreceptMode;

    public ZoteModel(float startX, float startY) {

        this.coordinateX = startX;
        this.coordinateY = startY;
        this.width = 50f;
        this.height = 80f;
        this.interactionRadius = 150f;
        this.interactionBounds = new Rectangle();
        this.dialogueIndex = 0;
        this.isPreceptMode = false;
        this.currentState = new ZoteIdleState();
        updateInteractionBounds();
    }

    public void update(float deltaTime, KnightModel knight) {
        checkProximity(knight);
        currentState.update(this, deltaTime);
    }

    private void updateInteractionBounds() {
        interactionBounds.set(
            coordinateX - interactionRadius,
            coordinateY,
            width + (interactionRadius * 2),
            height
        );
    }

    private void checkProximity(KnightModel knight) {

        Rectangle knightBounds = new Rectangle(

            (float) knight.getX(),
            (float) knight.getY(),
            (float) knight.getWidth(),
            (float) knight.getHeight()
        );
        canInteract = interactionBounds.overlaps(knightBounds);
    }

    public void interact() {

        if (canInteract) {

            currentState.interact(this);
        }
    }

    public void changeState(ZoteState newState) {

        this.currentState = newState;
        this.currentState.enter(this);
    }

    public void advanceDialogue() {

        if (!isPreceptMode) {

            dialogueIndex++;
            if (dialogueIndex >= 3) {

                isPreceptMode = true;
                dialogueIndex = 0;
                changeState(new ZoteIdleState());
            }
        }
        else {

            dialogueIndex++;
            changeState(new ZoteIdleState());
        }
    }

    public float getX() {
        return coordinateX;
    }
    public float getY() {
        return coordinateY;
    }
    public  float getWidth() {
        return width;
    }

    public boolean isCanInteract() {
        return canInteract;
    }
    public int getDialogueIndex() {
        return dialogueIndex;
    }
    public boolean isPreceptMode() {
        return isPreceptMode;
    }
    public ZoteState getCurrentState() {
        return currentState;
    }
}
