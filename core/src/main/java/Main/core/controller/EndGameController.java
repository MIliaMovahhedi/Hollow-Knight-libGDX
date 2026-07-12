package Main.core.controller;

import Main.core.model.GameState;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import Main.core.view.screen.GameScreen;

public class EndGameController {

    public void returnToMainMenu() {

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.resetGameSession();
        }

        GameState.getInstance().resetGameStats();

        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
