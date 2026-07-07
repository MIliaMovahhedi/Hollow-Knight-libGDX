package Main.core.view.screen;

import Main.core.controller.PauseMenuController;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class PauseMenuScreen extends MenuScreen {

    private final PauseMenuController controller;
    private TextButton resumeButton;
    private TextButton settingsButton;
    private TextButton mainMenuButton;

    public PauseMenuScreen() {
        controller = new PauseMenuController();
    }

    @Override
    protected void addButtonsAndListeners() {

        resumeButton = new TextButton("Resume", skin);
        settingsButton = new TextButton("Settings", skin);
        TextButton inventoryButton = new TextButton("Inventory", skin);
        mainMenuButton = new TextButton("Main Menu", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                controller.resumeGame();
            }
        });

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                ScreenManager.getInstance().navigateTo(ScreenState.INVENTORY_MENU);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                controller.goToSettings();
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                controller.goToMainMenu();
            }
        });

        table.add(resumeButton).spaceBottom(15).uniformX();
        table.row();
        table.add(inventoryButton).spaceBottom(15).uniformX();
        table.row();
        table.add(settingsButton).spaceBottom(15).uniformX();
        table.row();
        table.add(mainMenuButton).spaceBottom(15).uniformX();
    }

    @Override
    public void render(float deltaSeconds) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            controller.resumeGame();
            return;
        }

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.renderGameGraphics(0);
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        }

        stage.act(deltaSeconds);
        stage.draw();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

        resumeButton.setText(bundle.get("menu.resume"));
        settingsButton.setText(bundle.get("menu.settings"));
        mainMenuButton.setText(bundle.get("menu.mainMenu"));
    }
}
