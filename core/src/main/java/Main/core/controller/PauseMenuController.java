package Main.core.controller;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import Main.core.view.screen.GameScreen;

public class PauseMenuController {

    public void resumeGame() {

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {

            gameScreen.setPaused(false);
        }
        ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
    }
    public void goToMainMenu() {
        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);

        if (gameScreen != null) {
            gameScreen.setPaused(false);
            gameScreen.resetGameSession();
        }

        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
    public void goToSettings() {
        ScreenManager.getInstance().navigateTo(ScreenState.SETTINGS_MENU);
    }
}
