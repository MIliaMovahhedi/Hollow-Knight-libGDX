package Main.core.view.screen;

import Main.core.controller.EndGameController;
import Main.core.model.GameState;
import Main.core.model.GameStats;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class EndGameScreen extends MenuScreen {

    private final EndGameController controller;
    private TextButton mainMenuButton;

    public EndGameScreen() {
        super();
        controller = new EndGameController();
    }

    @Override
    protected void addButtonsAndListeners() {
        mainMenuButton = new TextButton("Return to Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.returnToMainMenu();
            }
        });
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_bg"));
        table.center();

        GameStats stats = GameState.getInstance().getGameStats();

        String headerText = stats.isFalseKnightDefeated() ? "VICTORY" : "GAME OVER";
        Label headerLabel = new Label(headerText, skin, "title");
        headerLabel.setFontScale(1.5f);

        table.add(headerLabel).colspan(2).padBottom(60).center().row();

        addStatRow("Play Time:", formatTime(stats.getPlayTime()));
        addStatRow("Total Kills:", String.valueOf(stats.getTotalKills()));
        addStatRow("Total Deaths:", String.valueOf(stats.getPlayerDeaths()));

        table.add(mainMenuButton).colspan(2).padTop(70).center();

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
    public void onLanguageChanged(I18NBundle bundle) {
        if (mainMenuButton != null && bundle != null) {
            mainMenuButton.setText(bundle.get("menu.main"));
        }
    }
}
