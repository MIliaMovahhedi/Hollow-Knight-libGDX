package Main.core.view.screen;

import Main.core.controller.GuideController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class GuideMenuScreen extends MenuScreen {

    private final GuideController controller;
    private TextButton backButton;

    public GuideMenuScreen() {
        controller = new GuideController();
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

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }
}
