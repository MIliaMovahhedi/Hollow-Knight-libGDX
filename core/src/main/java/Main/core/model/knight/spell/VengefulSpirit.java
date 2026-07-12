package Main.core.model.knight.spell;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.Set;

public class VengefulSpirit implements Spell {

    private double x;
    private double y;
    private double speed;
    private double lifeTimer;
    private final double maxLifetime = 2.0;
    private boolean isFacingRight;
    private boolean active;
    private Rectangle hitbox;
    private float width;
    private float height;

    private double startX;
    private double startY;
    private double blastTimer = 0.0;
    private Set<EnemyModel> damagedEnemies;

    public VengefulSpirit(double startX, double startY, double knightWidth, double knightHeight, boolean isFacingRight) {
        this.isFacingRight = isFacingRight;
        this.speed = 800.0;
        this.lifeTimer = 0.0;
        this.active = true;
        this.damagedEnemies = new HashSet<>();

        double spellSize = knightHeight;
        this.width = (float) spellSize * 2;
        this.height = (float) spellSize;

        double spawnX = isFacingRight ? startX + knightWidth : startX - width;
        this.startX = spawnX;
        this.startY = startY;

        this.x = spawnX;
        this.y = startY;
        this.hitbox = new Rectangle((float) x, (float) y, width, (float) spellSize);
    }

    @Override
    public void update(double deltaTime, LevelModel levelModel) {
        if (!active) return;

        blastTimer += deltaTime;
        lifeTimer += deltaTime;

        if (lifeTimer >= maxLifetime) {
            active = false;
            return;
        }

        double moveDirection = isFacingRight ? 1.0 : -1.0;
        x += speed * moveDirection * deltaTime;
        hitbox.setX((float) x);

        for (Main.core.model.enemy.EnemyModel enemy : levelModel.getEnemies()) {
            if (!enemy.isDead() && hitbox.overlaps(enemy.getBounds()) && !damagedEnemies.contains(enemy)) {
                enemy.takeDamage(3.0);
                damagedEnemies.add(enemy);
            }
        }

        for (Rectangle ground : levelModel.getGroundBounds()) {
            if (hitbox.overlaps(ground)) {
                active = false;
                break;
            }
        }
        for (Rectangle hazard : levelModel.getHazardBounds()) {
            if (hitbox.overlaps(hazard)) {
                active = false;
                break;
            }
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public Rectangle getHitbox() { return hitbox; }

    public boolean isFacingRight() { return isFacingRight; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public double getX() { return x; }
    public double getY() { return y; }

    public double getStartX() { return startX; }
    public double getStartY() { return startY; }
    public double getBlastTimer() { return blastTimer; }
}
