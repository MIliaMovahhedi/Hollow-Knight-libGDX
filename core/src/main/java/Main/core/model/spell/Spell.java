package Main.core.model.spell;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public interface Spell {

    void update(double deltaTime, LevelModel levelModel);
    boolean isActive();
    Rectangle getHitbox();
}
