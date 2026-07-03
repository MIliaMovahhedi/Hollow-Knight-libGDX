package Main.core.model.enemy;

import Main.core.model.LevelModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public abstract class EnemyModel {

    protected double x;
    protected double y;

    protected double width;
    protected double height;

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
    }

    public abstract void update(double deltaTime, KnightModel knightModel, LevelModel levelModel);

    public void changeState(EnemyState newState) {

        if (this.currentState != null) {

            this.currentState.exit(this);
        }
        this.currentState = newState;
        this.currentState.enter(this);
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
    public boolean canBeRemoved() {
        return isDead() && deathTimer >= DESPAWN_DURATION;
    }

    public void takeDamage(double amount) {
        this.health -= amount;
    }
    public boolean isDead() {
        return this.health <= 0;
    }
    public Rectangle getBounds() { return bounds; }
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
}
