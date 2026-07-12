package Main.core.controller;

import Main.core.controller.manager.SaveManager;
import Main.core.model.GameState;
import Main.core.model.SaveData;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import Main.core.view.screen.GameScreen;

import java.util.ArrayList;

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

    public void saveAndQuitGame() {
        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);

        if (gameScreen != null && gameScreen.getKnightModel() != null) {

            SaveData saveData = new SaveData();
            saveData.setMapIndex(gameScreen.getCurrentMapIndex());
            saveData.setKnightX(gameScreen.getKnightModel().getX());
            saveData.setKnightY(gameScreen.getKnightModel().getY());
            saveData.setCurrentHealth(gameScreen.getKnightModel().getCurrentHealth());
            saveData.setCurrentSoul(gameScreen.getKnightModel().getCurrentSoul());
            saveData.setEquippedCharms(gameScreen.getKnightModel().getCharmManager().getEquippedCharms());
            saveData.setGameStats(GameState.getInstance().getGameStats());
            saveData.setUnlockedAchievements(new ArrayList<>(Main.core.model.achievement.AchievementManager.getInstance().getUnlockedAchievements()));

            int slot = GameState.getInstance().getCurrentSlotIndex();
            SaveManager.getInstance().saveGame(saveData, slot);

            gameScreen.setPaused(false);
            gameScreen.resetGameSession();
        }
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
