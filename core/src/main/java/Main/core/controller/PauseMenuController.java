package Main.core.controller;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;

public class PauseMenuController {

    public void resumeGame() {
    }
    public void goToMainMenu() {
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
    public void goToSettings() {
        ScreenManager.getInstance().navigateTo(ScreenState.SETTINGS_MENU);
    }
}
