package Main.core.model;

import com.badlogic.gdx.math.Rectangle;

public class TransitionDoor {

    private final Rectangle bounds;
    private final int targetMapIndex;

    public TransitionDoor(Rectangle bounds, int targetMapIndex) {
        this.bounds = bounds;
        this.targetMapIndex = targetMapIndex;
    }

    public Rectangle getBounds() { return bounds; }
    public int getTargetMapIndex() { return targetMapIndex; }
}
