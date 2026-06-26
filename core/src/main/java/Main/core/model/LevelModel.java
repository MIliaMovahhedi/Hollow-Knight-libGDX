package Main.core.model;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LevelModel {

    private List<Rectangle> groundBounds;

    public LevelModel() {
        this.groundBounds = new ArrayList<>();
    }

    public void addGroundBound(Rectangle bound) {
        groundBounds.add(bound);
    }

    public List<Rectangle> getGroundBounds() {
        return groundBounds;
    }
}
