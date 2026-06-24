package Main.core.view.screen;

import Main.core.controller.AchievementsController;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

        table.add(backButton).spaceBottom(15).uniformX();
    }
}
