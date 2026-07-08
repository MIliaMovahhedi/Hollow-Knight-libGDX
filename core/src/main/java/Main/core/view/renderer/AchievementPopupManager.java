package Main.core.view.renderer;

import Main.core.controller.manager.AudioManager;
import Main.core.model.achievement.Achievement;
import Main.core.model.achievement.AchievementObserver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.LinkedList;
import java.util.Queue;

public class AchievementPopupManager implements AchievementObserver {

    private final Stage stage;
    private final Skin skin;
    private final Queue<Achievement> popupQueue;
    private boolean isAnimating;

    public AchievementPopupManager() {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/hollowNightSkin.json"));
        popupQueue = new LinkedList<>();
        isAnimating = false;
    }

    @Override
    public void onAchievementUnlocked(Achievement achievement) {

        popupQueue.add(achievement);
    }

    public void render(float deltaTime) {

        if (!isAnimating && !popupQueue.isEmpty()) {

            displayNextPopup(popupQueue.poll());
        }
        stage.act(deltaTime);
        stage.draw();
    }

    private void displayNextPopup(Achievement achievement) {

        isAnimating = true;

        AudioManager.getInstance().playSfx(AudioManager.getInstance().achivementPopUp);

        Table popupTable = new Table();
        popupTable.setBackground(skin.get("default", com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle.class).background);

        String iconName = getIconName(achievement);
        Image icon = new Image(skin.getDrawable(iconName));

        Table textTable = new Table();
        textTable.left();

        Label title = new Label("ACHIEVEMENT UNLOCKED", skin, "title");
        title.setFontScale(0.5f);
        title.setColor(Color.GOLD);

        Label name = new Label(getAchievementTitle(achievement), skin, "title");
        name.setFontScale(0.7f);

        textTable.add(title).left().padBottom(5).row();
        textTable.add(name).left();

        popupTable.add(icon).size(65, 65).pad(10);
        popupTable.add(textTable).padRight(20).left();

        popupTable.setSize(450, 100);

        float startX = 1920;
        float targetX = 1920 - popupTable.getWidth() - 20;
        float y = 20;

        popupTable.setPosition(startX, y);

        popupTable.addAction(Actions.sequence(
            Actions.moveTo(targetX, y, 0.6f, com.badlogic.gdx.math.Interpolation.exp10Out),
            Actions.delay(3.5f),
            Actions.moveTo(startX, y, 0.6f, com.badlogic.gdx.math.Interpolation.exp10In),
            Actions.run(() -> {
                popupTable.remove();
                isAnimating = false;
            })
        ));

        stage.addActor(popupTable);
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

    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {

        stage.dispose();
        skin.dispose();
    }
}
