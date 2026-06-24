package Main.core.view.screen;

import Main.core.controller.SettingController;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class SettingsMenuScreen extends MenuScreen {

    private final SettingController controller;
    private TextButton changeLanguageButton;
    private TextButton backButton;

    public SettingsMenuScreen() {
        controller = new SettingController();
    }

    @Override
    protected void addButtonsAndListeners() {
        changeLanguageButton = new TextButton("Change Language", skin);
        backButton = new TextButton("Back", skin);

        changeLanguageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.changeLanguage();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goBackToMainMenu();
            }
        });

        table.add(changeLanguageButton).spaceBottom(15).uniformX();
        table.row();
        table.add(backButton).spaceBottom(15).uniformX();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }
}
