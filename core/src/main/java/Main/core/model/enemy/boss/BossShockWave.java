package Main.core.model.enemy.boss;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public class BossShockWave {

    private double x, y;
    private final double width = 40.0, height = 60.0;
    private double velocityX;
    private final double acceleration = 250.0;
    private final boolean isFacingRight;
    private double lifeTimer = 0.0;
    private final double maxLifeTime = 3.0;
    private boolean active = true;
    private final Rectangle bounds;
    private boolean hasHitPlayer = false;

    public BossShockWave(double startX, double startY, boolean isFacingRight) {

        this.x = startX;
        this.y = startY;
        this.isFacingRight = isFacingRight;

        this.velocityX = isFacingRight ? 150.0 : -150.0;
        this.bounds = new Rectangle((float) x, (float) y, (float) width, (float) height);
    }

    public void update(double deltaTime, LevelModel  levelModel) {

        lifeTimer += deltaTime;
        if (lifeTimer >= maxLifeTime) {

            active = false;
            return;
        }

        double accelStep = isFacingRight ? acceleration : -acceleration;
        velocityX += accelStep * deltaTime;
        x += velocityX * deltaTime;

        bounds.setX((float) x);

        float probeSize = 5.0f;
        float probeX = isFacingRight ?
            (float) (x + width) :
            (float) (x - probeSize);
        float probeY = (float) y + 2.0f;
        float probeHeight = (float) height - 4.0f;

        Rectangle wallProbe = new Rectangle(probeX, probeY, probeSize, probeHeight);

        for (Rectangle wall : levelModel.getGroundBounds()) {

            if (wallProbe.overlaps(wall)) {

                active = false;
                break;
            }
        }
    }

    public boolean isActive() { return active; }
    public Rectangle getBounds() { return bounds; }
    public double getDamageAmount() { return 2.0; }

    public boolean hasHitPlayer() {
        return hasHitPlayer;
    }
    public void setHasHitPlayer() {
        this.hasHitPlayer = true;
    }
}
