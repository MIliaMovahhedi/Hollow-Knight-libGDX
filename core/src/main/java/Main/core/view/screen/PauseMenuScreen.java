package Main.core.view.screen;

import Main.core.controller.PauseMenuController;
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
        mainMenuButton = new TextButton("Main Menu", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.resumeGame();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goToSettings();
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goToMainMenu();
            }
        });

        table.add(resumeButton).spaceBottom(15).uniformX();
        table.row();
        table.add(settingsButton).spaceBottom(15).uniformX();
        table.row();
        table.add(mainMenuButton).spaceBottom(15).uniformX();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

        resumeButton.setText(bundle.get("menu.resume"));
        settingsButton.setText(bundle.get("menu.settings"));
        mainMenuButton.setText(bundle.get("menu.mainMenu"));
    }
}
