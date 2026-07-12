package Main.core.view.screen;

import Main.core.controller.EndGameController;
import Main.core.controller.StartGameController;
import Main.core.model.GameState;
import Main.core.model.GameStats;
import Main.core.model.achievement.AchievementManager;
import Main.core.view.renderer.AchievementPopupManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class EndGameScreen extends MenuScreen {

    private final EndGameController controller;
    private TextButton mainMenuButton;
    private TextButton restartButton;
    private AchievementPopupManager achievementPopupManager;

    public EndGameScreen() {
        super();
        controller = new EndGameController();
        achievementPopupManager = new AchievementPopupManager();
    }

    @Override
    public void show() {
        super.show();

        AchievementManager.getInstance().addObserver(achievementPopupManager);

        AchievementManager.getInstance().evaluateAchievements(GameState.getInstance().getGameStats());
    }

    @Override
    protected void addButtonsAndListeners() {
        mainMenuButton = new TextButton("Return to Menu", skin);
        restartButton = new TextButton("Restart Game", skin);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.returnToMainMenu();
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartGameController startController = new StartGameController();
                startController.startNewGame(GameState.getInstance().getCurrentSlotIndex());
            }
        });
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.center();

        GameStats stats = GameState.getInstance().getGameStats();
        String headerText = stats.isFalseKnightDefeated() ? "VICTORY" : "GAME OVER";
        Label headerLabel = new Label(headerText, skin, "title");
        headerLabel.setFontScale(1.5f);

        table.add(headerLabel).colspan(2).padBottom(60).center().row();

        addStatRow("Play Time:", formatTime(stats.getPlayTime()));
        addStatRow("Total Kills:", String.valueOf(stats.getTotalKills()));
        addStatRow("Total Deaths:", String.valueOf(stats.getPlayerDeaths()));

        table.add(restartButton).padTop(70).center().padRight(20);
        table.add(mainMenuButton).padTop(70).center();

        stage.addActor(table);
    }

    private void addStatRow(String labelText, String valueText) {
        Label label = new Label(labelText, skin, "desc");
        Label value = new Label(valueText, skin, "title");
        value.setFontScale(0.8f);

        table.add(label).right().padRight(20).padBottom(15);
        table.add(value).left().padBottom(15).row();
    }

    private String formatTime(float seconds) {
        int minutes = (int) (seconds / 60);
        int remainingSeconds = (int) (seconds % 60);
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @Override
    public void render(float deltaSeconds) {
        super.render(deltaSeconds);

        if (achievementPopupManager != null) {
            achievementPopupManager.render(deltaSeconds);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (achievementPopupManager != null) {
            achievementPopupManager.resize(width, height);
        }
    }

    @Override
    public void hide() {
        super.hide();

        AchievementManager.getInstance().removeObserver(achievementPopupManager);
    }

    @Override
    public void dispose() {
        if (achievementPopupManager != null) {
            achievementPopupManager.dispose();
        }
        super.dispose();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (mainMenuButton != null && bundle != null) {
            mainMenuButton.setText("Main Menu");
            restartButton.setText("Restart Game");
        }
    }
}
