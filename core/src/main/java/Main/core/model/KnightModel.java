package Main.core.model;

import Main.core.controller.GameController;
import Main.core.model.knightState.AttackState;
import Main.core.model.knightState.IdleState;
import Main.core.model.knightState.KnightState;
import com.badlogic.gdx.math.Rectangle;

public class KnightModel {

    private double x;
    private double y;
    private final double startX;
    private final double startY;
    private double lastSafeX;
    private double lastSafeY;
    private double xVelocity;
    private double yVelocity;
    private double width;
    private double height;

    private double maxHealth;
    private double currentHealth;
    private int currentSoul;
    private double invincibilityDuration;
    private double invincibilityTimer;
    private boolean isInvincible;
    private double damageAmount;

    private boolean isGrounded;
    private boolean canDoubleJump;
    private boolean canDash = true;
    private boolean isFacingRight = true;

    private final double GRAVITY = -900.0;
    private final double MAX_SPEED = 300.0;
    private final double MAX_JUMP_SPEED = 600.0;
    private final double DASH_SPEED = 800f;
    private final double DASH_DURATION = 0.2f;
    private final int MAX_SOUL = 99;
    private final int SOUL_PER_HIT = 11;
    private final int SOUL_PER_ACTION = 33;
    private final double ATTACK_RANGE = 40.0;
    private final double POGO_BOUNCE_SPEED = 750.0;

    private Rectangle bounds;

    private KnightState knightState;

    public KnightModel(double startX, double startY) {

        knightState = new IdleState();

        this.x = startX;
        this.y = startY;
        this.startX = startX;
        this.startY = startY;
        this.xVelocity = 0.0;
        this.yVelocity = 0.0;
        this.width = 32.0;
        this.height = 40.0;

        this.maxHealth = 5;
        this.currentHealth = this.maxHealth;
        this.currentSoul = 0;
        this.invincibilityDuration = 1.0;
        this.invincibilityTimer = 0.0;
        this.isInvincible = false;
        this.damageAmount = 1.0;

        this.bounds = new Rectangle((float) x, (float) y, (float) width, (float) height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public double getMaxSpeed() {
        return MAX_SPEED;
    }
    public double getMaxJumpSpeed() {
        return MAX_JUMP_SPEED;
    }
    public double getGravity() {
        return GRAVITY;
    }

    public boolean isGrounded() {
        return isGrounded;
    }
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    public boolean canDash() {
        return canDash;
    }
    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    public double getDashDuration() {
        return DASH_DURATION;
    }
    public double getDashSpeed() {
        return DASH_SPEED;
    }

    public boolean canDoubleJump() {
        return canDoubleJump;
    }
    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }
    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getXVelocity() {
        return xVelocity;
    }
    public double getYVelocity() {
        return yVelocity;
    }

    private boolean takeDamage() {

        if (isInvincible) return true;

        currentHealth -= damageAmount;
        if (currentHealth <= 0) {

            handleDeath();
            return false;
        }
        else {

            isInvincible = true;
            invincibilityTimer = invincibilityDuration;
            return true;
        }
    }
    private void handleDeath() {

        currentHealth = maxHealth;
        this.x = 100.0;
        this.y = 100.0;
        this.xVelocity = 0;
        this.yVelocity = 0;
        bounds.setX((float) x);
        bounds.setY((float) y);
    }

    public int getCurrentSoul() {
        return currentSoul;
    }
    public double getCurrentHealth() {
        return currentHealth;
    }
    public void addSoul() {
        currentSoul = Math.min(currentSoul + SOUL_PER_HIT, MAX_SOUL);
    }
    public boolean consumeSoul() {

        if (currentSoul >= SOUL_PER_ACTION) {

            currentSoul -= SOUL_PER_ACTION;
            return true;
        }
        return false;
    }
    public void heal() {

        if (currentHealth < maxHealth) {

            currentHealth++;
        }
    }

    public void setKnightState(KnightState knightState) {
        this.knightState = knightState;
        knightState.enter(this);
    }
    public void update(GameController gameController, LevelModel levelModel, double deltaTime) {

        if (gameController.isOnAttack() && !(this.knightState instanceof AttackState)) {

            boolean isDown = gameController.isOnDownMove() && !isGrounded;

            setKnightState(new AttackState(isDown));
            executeAttack(isDown, levelModel);
        }

        KnightState newState = this.knightState.update(this, gameController, deltaTime);

        if (newState != this.knightState) {

            setKnightState(newState);
        }

        yVelocity += GRAVITY * deltaTime;

        handleGroundBounds(deltaTime, levelModel);

        if (isInvincible) {

            invincibilityTimer -= deltaTime;
            if (invincibilityTimer <= 0) {

                isInvincible = false;
                invincibilityTimer = 0.0;
            }
        }

        if (isGrounded) {
            lastSafeX = this.x;
            lastSafeY = this.y;
        }

        handleHazardBounds(levelModel);
    }

    private void respawnAtLastSafePlatform() {
        this.x = lastSafeX;
        this.y = lastSafeY;
        this.xVelocity = 0;
        this.yVelocity = 0;
        bounds.setX((float) x);
        bounds.setY((float) y);
    }
    private void respawnAtStart() {
        this.x = startX;
        this.y = startY;
    }
    private void handleGroundBounds(double deltaTime, LevelModel levelModel) {

        double nextX = this.x + (xVelocity * deltaTime);
        bounds.setX((float) nextX);

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (bounds.overlaps(rect)) {

                if (xVelocity > 0) {

                    nextX = rect.getX() - bounds.getWidth();
                }
                else if (xVelocity < 0) {

                    nextX = rect.getX() + rect.getWidth();
                }

                xVelocity = 0;
                bounds.setX((float) nextX);
                break;
            }
        }
        this.x = nextX;

        double nextY = this.y + (yVelocity * deltaTime);
        bounds.setY((float) nextY);

        isGrounded = false;

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (bounds.overlaps(rect)) {

                if (yVelocity > 0) {

                    nextY = rect.getY() - bounds.getHeight();
                    yVelocity = 0;
                }
                else if (yVelocity < 0) {

                    nextY = rect.getY() + rect.getHeight();
                    yVelocity = 0;
                    isGrounded = true;
                    canDoubleJump = true;
                    canDash = true;
                }
                bounds.setY((float) nextY);
                break;
            }
        }
        this.y = nextY;
    }
    private void handleHazardBounds(LevelModel levelModel) {

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (bounds.overlaps(hazard)) {

                if (!isInvincible) {

                    boolean survived = takeDamage();
                    if (survived) {

                        respawnAtLastSafePlatform();
                    }
                    else {

                        respawnAtStart();
                    }
                }
                break;
            }
        }
    }

    private void executeAttack(boolean isDown, LevelModel levelModel) {

        Rectangle attackHitbox;

        if (isDown) {

            attackHitbox = new Rectangle((float) x, (float) (y - ATTACK_RANGE), (float) width, (float) ATTACK_RANGE);
        }
        else {

            float hitX = isFacingRight ? (float) (x + width) : (float) (x - ATTACK_RANGE);
            attackHitbox = new Rectangle(hitX, (float) y, (float) ATTACK_RANGE, (float) height);
        }

        checkAttackCollisions(attackHitbox, isDown, levelModel);
    }

    private void checkAttackCollisions(Rectangle attackHitbox, boolean isDown, LevelModel levelModel) {

        boolean hitSuccessful = false;

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (attackHitbox.overlaps(hazard)) {

                hitSuccessful = true;
                if (isDown) {

                    applyPogoBounce();
                }
            }
        }

        if (hitSuccessful) {
            addSoul();
        }
    }

    private void applyPogoBounce() {

        yVelocity = POGO_BOUNCE_SPEED;
        canDash = true;
        canDoubleJump = true;
    }
}
