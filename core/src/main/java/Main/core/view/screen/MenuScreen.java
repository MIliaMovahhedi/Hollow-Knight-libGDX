package Main.core.view.screen;

import Main.core.model.LocalizationManager;
import Main.core.view.Translatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class MenuScreen implements Screen, Translatable {

    protected Stage stage;
    protected Table table;
    protected Skin skin;

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
        table = new Table();

        initializeSkin();
        addButtonsAndListeners();
        createTableLayout();
        enableStageInput();

        LocalizationManager.getInstance().register(this);
    }

    protected void initializeSkin() {

        skin = new Skin(Gdx.files.internal("ui/Hollow Knight skin.json"));
    }
    protected void addButtonsAndListeners() {
    }
    protected void createTableLayout() {

        table.setFillParent(true);
        table.center();
        stage.addActor(table);
    }
    protected void enableStageInput() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaSeconds) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaSeconds);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        Gdx.input.setInputProcessor(null);
        LocalizationManager.getInstance().unregister(this);
    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();
    }
}
