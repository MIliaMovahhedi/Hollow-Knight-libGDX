package Main.core.model.spell;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public class VengefulSpirit implements Spell {

    private double x;
    private double y;
    private double speed;
    private double lifeTimer;
    private final double maxLifetime = 2.0;
    private boolean isFacingRight;
    private boolean active;
    private Rectangle hitbox;

    public VengefulSpirit(double startX, double startY, boolean isFacingRight) {

        this.isFacingRight = isFacingRight;
        this.speed = 800.0;
        this.lifeTimer = 0.0;
        this.active = true;

        double spawnX = isFacingRight ? startX + 32.0 : startX - 40.0;
        this.x = spawnX;
        this.y = startY;
        this.hitbox = new Rectangle((float) x, (float) y, 40.0f, 40.0f);
    }

    @Override
    public void update(double deltaTime, LevelModel levelModel) {

        if (!active)
            return;

        lifeTimer += deltaTime;
        if (lifeTimer >= maxLifetime) {

            active = false;
            return;
        }

        double moveDirection = isFacingRight ? 1.0 : -1.0;
        x += speed * moveDirection * deltaTime;
        hitbox.setX((float) x);

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (hitbox.overlaps(hazard)) {

                active = false;
                break;
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
