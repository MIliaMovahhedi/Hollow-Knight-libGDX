package Main.core.view.screen;

import Main.core.controller.AchievementsController;
import Main.core.model.achievement.Achievement;
import Main.core.model.achievement.AchievementManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class AchievementsMenuScreen extends MenuScreen {

    private final AchievementsController controller;
    private TextButton backButton;

    public AchievementsMenuScreen() {
        controller = new AchievementsController();
    }

    @Override
    protected void addButtonsAndListeners() {

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToMainMenu();
            }
        });
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.top().padTop(100);

        Label mainTitle = new Label("ACHIEVEMENTS", skin, "title");
        mainTitle.setFontScale(1.2f);
        table.add(mainTitle).colspan(2).padBottom(40).center().row();

        Table listTable = new Table();

        for (Achievement achievement : Achievement.values()) {

            boolean isUnlocked = AchievementManager.getInstance().isUnlocked(achievement);
            addAchievementRow(listTable, achievement, isUnlocked);
        }

        table.add(listTable).colspan(2).center().row();
        table.add(backButton).colspan(2).padTop(50).center();

        stage.addActor(table);
    }

    private void addAchievementRow(Table parentTable, Achievement achievement, boolean isUnlocked) {

        String baseIconName = getIconName(achievement);
        String iconName = isUnlocked ? baseIconName : baseIconName + "_locked";

        Image icon = new Image(skin.getDrawable(iconName));

        Table textTable = new Table();
        textTable.left();

        String titleText = getAchievementTitle(achievement);
        String descText = getAchievementDesc(achievement);

        Label titleLabel = new Label(titleText.toUpperCase(), skin, "title");
        titleLabel.setFontScale(0.8f);

        Label descLabel = new Label(descText, skin, "desc");
        descLabel.setFontScale(0.6f);

        textTable.add(titleLabel).left().padBottom(3).row();
        textTable.add(descLabel).left();

        parentTable.add(icon).size(75, 75).pad(10).padBottom(30).right();
        parentTable.add(textTable).left().padLeft(20).expandX().fillX().padBottom(30).row();
    }

    private String getIconName(Achievement achievement) {
        return switch (achievement) {
            case COMPLETION -> "achievementCompletion";
            case SPEEDRUN -> "achievementSpeedRun";
            case TRUE_HUNTER -> "achievementTrueHunter";
            case DEFEAT_FALSE_KNIGHT -> "achievementFalseKnight";
            case UNTOUCHABLE -> "achievementUntouchable";
        };
    }

    private String getAchievementTitle(Achievement achievement) {
        return switch (achievement) {
            case COMPLETION -> "Completion";
            case SPEEDRUN -> "Speedrun";
            case TRUE_HUNTER -> "True Hunter";
            case DEFEAT_FALSE_KNIGHT -> "False Knight";
            case UNTOUCHABLE -> "Untouchable";
        };
    }

    private String getAchievementDesc(Achievement achievement) {

        return switch (achievement) {

            case COMPLETION -> "Successfully finish the game";
            case SPEEDRUN -> "Finish the game under the one hour";
            case TRUE_HUNTER -> "Defeat every type of enemy";
            case DEFEAT_FALSE_KNIGHT -> "Conquer the False Knight boss";
            case UNTOUCHABLE -> "Complete the game with zero deaths";
        };
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (backButton != null && bundle != null) {
            backButton.setText(bundle.get("menu.back"));
        }
    }
}
