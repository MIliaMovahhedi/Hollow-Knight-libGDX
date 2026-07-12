package Main.core.model;

import Main.core.model.enums.Language;
import Main.core.model.enums.ScreenState;

public class GameState {

    private static GameState instance;
    private ScreenState currentScreen = ScreenState.MAIN_MENU;
    private GameStats gameStats;
    private int currentSlotIndex = 1;

    private GameState() {
        this.gameStats = new GameStats();
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void setScreen(ScreenState currentScreen) {
        this.currentScreen = currentScreen;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    public void setGameStats(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    public void resetGameStats() {
        this.gameStats = new GameStats();
    }

    public int getCurrentSlotIndex() {
        return currentSlotIndex;
    }

    public void setCurrentSlotIndex(int currentSlotIndex) {
        this.currentSlotIndex = currentSlotIndex;
    }
}
