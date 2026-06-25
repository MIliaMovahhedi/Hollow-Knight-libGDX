package Main.core.model;

import Main.core.controller.GameController;
import Main.core.model.knightState.KnightState;

public class KnightModel {

    private double x;
    private double y;
    private double xVelocity;
    private double yVelocity;

    private boolean isGrounded;
    private boolean canDoubleJump;
    private boolean canDash = true;
    private boolean isFacingRight = true;

    final double GRAVITY = -900.0;
    final double MAX_SPEED = 300.0;
    final double MAX_JUMP_SPEED = 600.0;
    final double DASH_SPEED = 800f;
    final double DASH_DURATION = 0.2f;

    public KnightState knightState;

    public KnightModel() {
        this.x = 0.0;
        this.y = 0.0;
        this.xVelocity = 0.0;
        this.yVelocity = 0.0;
    }

    public double getMaxSpeed() {
        return MAX_SPEED;
    }
    public double getMaxJumpSpeed() {
        return MAX_JUMP_SPEED;
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
    public void update(GameController gameController, double deltaTime) {

        KnightState newState = this.knightState.update(this, gameController, deltaTime);

        if (newState != this.knightState) {

            setKnightState(newState);
        }

        yVelocity += GRAVITY * deltaTime;

        this.x += xVelocity * deltaTime;
        this.y += yVelocity * deltaTime;

        if (y <= 0.0) {
            y = 0.0;
            yVelocity = 0.0;
            isGrounded = true;
            canDoubleJump = true;
            canDash = true;
        }
        else {

            isGrounded = false;
        }
    }
}
