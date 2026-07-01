package Main.core.model.knight.spell;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public class HowlingWraiths implements Spell {

    private final Rectangle hitbox;
    private double timer;
    private final double duration = 1.0;
    private int tickCount;
    private boolean active;
    private final double spawnX;
    private final double spawnY;
    double spellWidth;
    double spellHeight;

    public HowlingWraiths(double startX, double startY, double knightWidth, double knightHeight) {

        this.active = true;
        this.timer = 0.0;
        this.tickCount = 0;

        spellWidth = knightWidth * 2.25;
        spellHeight = knightHeight * 2.5;

        double offsetX = (spellWidth - knightWidth) / 2.0;

        this.hitbox = new Rectangle(
            (float) (spawnX = startX - offsetX),
            (float) (spawnY = startY),
            (float) spellWidth,
            (float) spellHeight
        );
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

    public float getWidth() {
        return (float) spellWidth;
    }
    public float getHeight() {
        return (float) spellHeight;
    }

    public double getX() {
        return spawnX;
    }
    public double getY() {
        return spawnY;
    }

    public double getTimer() {
        return timer;
    }
}
