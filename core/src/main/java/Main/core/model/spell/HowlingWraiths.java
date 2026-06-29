package Main.core.model.spell;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public class HowlingWraiths implements Spell {

    private Rectangle hitbox;
    private double timer;
    private final double duration = 1.0;
    private int tickCount;
    private boolean active;

    public HowlingWraiths(double startX, double startY, double knightWidth) {

        this.active = true;
        this.timer = 0.0;
        this.tickCount = 0;
        this.hitbox = new Rectangle((float) (startX - 20), (float) (startY + 40), (float) (knightWidth + 40), 100.0f);
    }

    @Override
    public void update(double deltaTime, LevelModel levelModel) {

        if (!active)
            return;

        timer += deltaTime;

        int expectedTicks = (int) ((timer / duration) * 3);

        if (expectedTicks > tickCount && expectedTicks <= 3) {

            applyDamageTick(levelModel);
            tickCount = expectedTicks;
        }

        if (timer >= duration) {

            active = false;
        }
    }

    private void applyDamageTick(LevelModel levelModel) {

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (hitbox.overlaps(hazard)) {

            }
        }
    }

    @Override
    public boolean isActive() {

        return active;
    }

    @Override
    public Rectangle getHitbox() {

        return hitbox;
    }
}
