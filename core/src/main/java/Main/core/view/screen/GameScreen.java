package Main.core.view.screen;

import Main.core.controller.GameController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;

public class GameScreen extends MenuScreen {

    private GameController controller;

    private int fixedFrameRate;
    private double fixedDeltaTime;
    private double accumulator;

    public GameScreen() {
        this.controller = new GameController();
    }

    @Override
    public void show() {
        super.show();

        accumulator = 0;
        fixedFrameRate = 120;
        fixedDeltaTime = (double) 1 / fixedFrameRate;
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }

    @Override
    public void render(float deltaSeconds) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update();

        accumulator += deltaSeconds;

        while (accumulator >= fixedDeltaTime) {

            accumulator -= fixedDeltaTime;
        }
    }
}
