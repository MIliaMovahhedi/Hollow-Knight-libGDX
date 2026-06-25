package Main.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameController {

    private boolean onLeftMove;
    private boolean onRightMove;
    private boolean onJump;
    private boolean onDash;

    public void update() {

        onLeftMove = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        onRightMove = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        onJump = Gdx.input.isKeyPressed(Input.Keys.Z);
        onDash = Gdx.input.isKeyPressed(Input.Keys.C);
    }

    public boolean isOnLeftMove() {
        return onLeftMove;
    }
    public boolean isOnRightMove() {
        return onRightMove;
    }
    public boolean isOnJump() {
        return onJump;
    }
    public boolean isOnDash() {
        return onDash;
    }
}
