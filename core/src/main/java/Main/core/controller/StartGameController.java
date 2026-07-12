package Main.core.controller;

import Main.core.controller.manager.SaveManager;
import Main.core.model.GameState;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import Main.core.view.screen.GameScreen;
import Main.core.view.screen.MainMenuScreen;

public class StartGameController {

    public void startNewGame() {
        int nextAvailableSlot = 1;

        for (int i = 1; i <= 4; i++) {
            if (SaveManager.getInstance().isSlotEmpty(i)) {
                nextAvailableSlot = i;
                break;
            }
        }

        GameState.getInstance().setCurrentSlotIndex(nextAvailableSlot);
        GameState.getInstance().resetGameStats();
        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.resetGameSession();
        }
        ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
    }

    public void startNewGame(int slotIndex) {

        GameState.getInstance().setCurrentSlotIndex(slotIndex);
        GameState.getInstance().resetGameStats();
        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.resetGameSession();
        }
        ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
    }

    public void loadGame(int slotIndex) {
        GameState.getInstance().setCurrentSlotIndex(slotIndex);
        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.resetGameSession();
        }
        ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
    }

    public void goBackToMainMenu() {
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
