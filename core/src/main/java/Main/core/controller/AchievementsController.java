package Main.core.controller;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;

public class AchievementsController {

    public void goBackToMainMenu() {
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
