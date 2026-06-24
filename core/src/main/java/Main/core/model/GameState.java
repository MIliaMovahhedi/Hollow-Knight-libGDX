package Main.core.model;

import Main.core.model.enums.Language;
import Main.core.model.enums.ScreenState;

public class GameState {

    private ScreenState currentScreen = ScreenState.MAIN_MENU;
    public void setScreen(ScreenState currentScreen) {
        this.currentScreen = currentScreen;
    }
}
