package Main.core.model.enemy;

import Main.core.controller.manager.AudioManager;
import Main.core.model.GameState;
import Main.core.model.LevelModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public abstract class EnemyModel {

    protected double x;
    protected double y;

    protected float distanceToPlayer;

    protected double width;
    protected double height;

    protected boolean isDynamicHitboxActive = false;
    protected double dynamicWidth;
    protected double dynamicHeight;
    protected Rectangle dynamicBounds = new Rectangle();

    protected double xVelocity;
    protected double yVelocity;

    protected double health;
    protected double maxHealth;
    protected double damageAmount;

    protected boolean isFacingRight;
    protected boolean isGrounded;
    protected Rectangle bounds;
    protected EnemyState currentState;

    protected double deathTimer = 0.0;
    protected final double DESPAWN_DURATION = 1.5;

    public EnemyModel(double x, double y, double width, double height, double maxHealth, double damageAmount) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damageAmount = damageAmount;
        this.isFacingRight = false;
        this.bounds = new Rectangle((float) x, (float) y, (float) width, (float) height);

        this.distanceToPlayer = 10000.0f;
    }

    public abstract void update(double deltaTime, KnightModel knightModel, LevelModel levelModel);

    public void changeState(EnemyState newState) {

        if (this.currentState != null) {

            this.currentState.exit(this);
        }

        this.currentState = newState;

        if (this.currentState != null) {

            this.currentState.enter(this);
        }
    }

    public void applyGravity(double gravity, double deltaTime) {

        this.yVelocity += gravity * deltaTime;
    }

    public void updateDeath(double deltaTime) {

        if (isDead()) {
            this.xVelocity = 0;
            this.deathTimer += deltaTime;
        }
    }

    public void activateDynamicHitbox(double newWidth, double newHeight) {
        this.dynamicWidth = newWidth;
        this.dynamicHeight = newHeight;
        this.isDynamicHitboxActive = true;
    }
    public void deactivateDynamicHitbox() {
        this.isDynamicHitboxActive = false;
    }

    public boolean canBeRemoved() {
        return false;
    }

    public void takeDamage(double amount) {

        AudioManager.getInstance().playSfx(AudioManager.getInstance().enemyDamageSound);
        this.health -= amount;

        if (isDead()) {

            GameState.getInstance().getGameStats().incrementTotalKills();
            GameState.getInstance().getGameStats().addDefeatedEnemyType(this.getClass().getSimpleName());
        }
    }
    public boolean isDead() {
        return this.health <= 0;
    }
    public boolean isGrounded() {
        return isGrounded;
    }
    public Rectangle getBounds() {

        if (isDynamicHitboxActive) {

            float hitX = isFacingRight ?
                (float) x :
                (float) (x - (dynamicWidth - width));

            dynamicBounds.set(hitX, (float) y, (float) dynamicWidth, (float) dynamicHeight);
            return dynamicBounds;
        }
        return bounds;
    }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isFacingRight() { return isFacingRight; }
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }
    public double getXVelocity() { return xVelocity; }
    public void setXVelocity(double xVelocity) { this.xVelocity = xVelocity; }
    public double getYVelocity() { return yVelocity; }
    public void setYVelocity(double yVelocity) { this.yVelocity = yVelocity; }
    public double getDamageAmount() { return damageAmount; }
    public EnemyState getCurrentState() {
        return currentState;
    }

    public void calculateDistanceToPlayer(KnightModel knight) {
        float dx = (float) (this.x - knight.getX());
        float dy = (float) (this.y - knight.getY());
        this.distanceToPlayer = (float) Math.sqrt((dx * dx) + (dy * dy));
    }
    public float getDistanceToPlayer() {
        return distanceToPlayer;
    }
}
