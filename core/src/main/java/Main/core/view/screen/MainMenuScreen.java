package Main.core.view.screen;

import Main.core.controller.MainMenuController;
import Main.core.controller.manager.AudioManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class MainMenuScreen extends MenuScreen {

    private final MainMenuController controller;
    private Image gameLogo;
    private TextButton startGameButton;
    private TextButton settingsButton;
    private TextButton guideButton;
    private TextButton achievementButton;
    private TextButton quitGameButton;

    public MainMenuScreen() {
        controller = new MainMenuController();
    }


    @Override
    public void show() {
        super.show();
        AudioManager.getInstance().playMusic(AudioManager.getInstance().menuMusic, true);
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.center();
        stage.addActor(table);
    }

    @Override
    protected void addButtonsAndListeners() {

        gameLogo = new Image(skin.getDrawable("game_logo"));

        startGameButton = new TextButton("Start Game", skin);
        settingsButton = new TextButton("Settings", skin);
        guideButton = new TextButton("Guide", skin);
        achievementButton = new TextButton("Achievement", skin);
        quitGameButton = new TextButton("Quit Game", skin);

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
        achievementButton.addListener(new ClickListener() {
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

        table.add(gameLogo).spaceBottom(20).row();
        table.add(startGameButton).spaceBottom(15).uniformX().row();
        table.add(settingsButton).spaceBottom(15).uniformX().row();
        table.add(guideButton).spaceBottom(15).uniformX().row();
        table.add(achievementButton).spaceBottom(15).uniformX().row();
        table.add(quitGameButton).spaceBottom(15).uniformX().row();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

        startGameButton.setText(bundle.get("menu.startGame"));
        settingsButton.setText(bundle.get("menu.settings"));
        guideButton.setText(bundle.get("menu.guide"));
        achievementButton.setText(bundle.get("menu.achievements"));
        quitGameButton.setText(bundle.get("menu.quitGame"));
    }
}
