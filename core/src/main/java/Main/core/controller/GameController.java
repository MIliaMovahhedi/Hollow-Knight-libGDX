package Main.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameController {

    private boolean onLeftMove;
    private boolean onRightMove;
    private boolean onJump;
    private boolean onDash;
    private boolean onAttack;
    private boolean onFocus;
    private boolean onDownMove;

    public void update() {

        onLeftMove = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        onRightMove = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        onJump = Gdx.input.isKeyJustPressed(Input.Keys.Z);
        onDash = Gdx.input.isKeyJustPressed(Input.Keys.C);
        onAttack = Gdx.input.isKeyJustPressed(Input.Keys.X);
        onFocus = Gdx.input.isKeyPressed(Input.Keys.A);
        onDownMove = Gdx.input.isKeyPressed(Input.Keys.DOWN);
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
    public boolean isOnAttack() {
        return onAttack;
    }
    public boolean isOnFocus() {
        return onFocus;
    }
    public boolean isOnDownMove() {
        return onDownMove;
    }
}
