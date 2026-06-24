package Main.core.view.screen;

import Main.core.controller.MainMenuController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class MainMenuScreen extends MenuScreen {

    private final MainMenuController controller;
    private TextButton startGameButton;
    private TextButton settingsButton;
    private TextButton guideButton;
    private TextButton AchievementButton;
    private TextButton quitGameButton;

    public MainMenuScreen() {
        controller = new MainMenuController();
    }

    @Override
    protected void addButtonsAndListeners() {

        startGameButton = new TextButton("Start Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton guideButton = new TextButton("Guide", skin);
        TextButton AchievementButton = new TextButton("Achievement", skin);
        TextButton quitGameButton = new TextButton("Quit Game", skin);

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goToStartGameMenu();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goToSettings();
            }
        });
        guideButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goToGuideMenu();
            }
        });
        AchievementButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goToAchievementMenu();
            }
        });
        quitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.quitGame();
            }
        });

        table.add(startGameButton).spaceBottom(15).uniformX();
        table.row();
        table.add(settingsButton).spaceBottom(15).uniformX();
        table.row();
        table.add(guideButton).spaceBottom(15).uniformX();
        table.row();
        table.add(AchievementButton).spaceBottom(15).uniformX();
        table.row();
        table.add(quitGameButton).spaceBottom(15).uniformX();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }
}
