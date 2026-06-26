package Main.core.model;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LevelModel {

    private List<Rectangle> groundBounds;
    private List<Rectangle> hazardBounds;

    public LevelModel() {
        this.groundBounds = new ArrayList<>();
        this.hazardBounds = new ArrayList<>();
    }

    public void addGroundBound(Rectangle bound) {
        groundBounds.add(bound);
    }
    public void addHazardBound(Rectangle bound) {
        hazardBounds.add(bound);
    }

    public List<Rectangle> getGroundBounds() {
        return groundBounds;
    }
    public List<Rectangle> getHazardBounds() {
        return hazardBounds;
    }
}
