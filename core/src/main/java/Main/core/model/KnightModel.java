package Main.core.model;

import Main.core.controller.GameController;
import Main.core.model.knightState.KnightState;
import com.badlogic.gdx.math.Rectangle;

public class KnightModel {

    private double x;
    private double y;
    private double lastSafeX;
    private double lastSafeY;
    private double xVelocity;
    private double yVelocity;
    private double width;
    private double height;

    private boolean isGrounded;
    private boolean canDoubleJump;
    private boolean canDash = true;
    private boolean isFacingRight = true;

    final double GRAVITY = -900.0;
    final double MAX_SPEED = 300.0;
    final double MAX_JUMP_SPEED = 600.0;
    final double DASH_SPEED = 800f;
    final double DASH_DURATION = 0.2f;

    private Rectangle bounds;

    private KnightState knightState;

    public KnightModel() {
        this.x = 100.0;
        this.y = 100.0;
        this.xVelocity = 0.0;
        this.yVelocity = 0.0;
        this.width = 32.0;
        this.height = 40.0;

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

    public void setKnightState(KnightState knightState) {
        this.knightState = knightState;
        knightState.enter(this);
    }
    public void update(GameController gameController, LevelModel levelModel, double deltaTime) {

        KnightState newState = this.knightState.update(this, gameController, deltaTime);

        if (newState != this.knightState) {

            setKnightState(newState);
        }

        yVelocity += GRAVITY * deltaTime;

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

        if (isGrounded) {
            lastSafeX = this.x;
            lastSafeY = this.y;
        }

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (bounds.overlaps(hazard)) {

                respawnAtLastSafePlatform();
                break;
            }
        }
    }

    private void respawnAtLastSafePlatform() {
        this.x = lastSafeX;
        this.y = lastSafeY;
        this.xVelocity = 0;
        this.yVelocity = 0;
        bounds.setX((float) x);
        bounds.setY((float) y);
    }
}
