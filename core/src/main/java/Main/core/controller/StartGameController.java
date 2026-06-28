package Main.core.controller;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import Main.core.view.screen.MainMenuScreen;

public class StartGameController {

    public void startNewGame() {

        ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
    }
    public void loadGame() {}
    public void goBackToMainMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
