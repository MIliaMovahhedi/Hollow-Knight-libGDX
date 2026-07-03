package Main.core.model.knight.spell;

import Main.core.model.LevelModel;
import com.badlogic.gdx.math.Rectangle;

public class VengefulSpirit implements Spell {

    private double x;
    private double y;
    private double speed;

    private double lifeTimer;
    private final double maxLifetime = 2.0;

    private boolean isFacingRight;

    private boolean isExploding;
    private double explodeTimer = 0.0;
    private final double explodeDuration = 0.8;
    private boolean active;

    private Rectangle hitbox;
    private float width;
    private float height;

    public VengefulSpirit(double startX, double startY, double knightWidth, double knightHeight, boolean isFacingRight) {

        this.isFacingRight = isFacingRight;
        this.speed = 800.0;
        this.lifeTimer = 0.0;
        this.active = true;
        this.isExploding = false;

        double spellSize = knightHeight;

        width = (float) spellSize * 2;
        height = (float) spellSize;

        double spawnX = isFacingRight ? startX + knightWidth : startX - width;

        this.x = spawnX;
        this.y = startY;
        this.hitbox = new Rectangle((float) x, (float) y, width, (float) spellSize);
    }
    @Override
    public void update(double deltaTime, LevelModel levelModel) {

        if (!active)
            return;

        if (isExploding) {

            explodeTimer += deltaTime;
            if (explodeTimer >= explodeDuration) {

                active = false;
            }

            return;
        }

        lifeTimer += deltaTime;
        if (lifeTimer >= maxLifetime) {

            startExplosion();
            return;
        }

        double moveDirection = isFacingRight ? 1.0 : -1.0;
        x += speed * moveDirection * deltaTime;
        hitbox.setX((float) x);

        for (Main.core.model.enemy.EnemyModel enemy : levelModel.getEnemies()) {

            if (!enemy.isDead() && hitbox.overlaps(enemy.getBounds())) {

                enemy.takeDamage(3.0);
                startExplosion();
                return;
            }
        }

        for (Rectangle ground : levelModel.getGroundBounds()) {
            if (hitbox.overlaps(ground)) {
                startExplosion();
                break;
            }
        }

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (hitbox.overlaps(hazard)) {

                startExplosion();
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

    private void startExplosion() {
        isExploding = true;
    }
    public boolean isExploding() {
        return isExploding;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getExplodeTimer() {
        return explodeTimer;
    }
}
