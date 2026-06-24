package Main.core.model;

import Main.core.model.enums.Language;
import Main.core.model.enums.ScreenState;

public class GameState {

    private ScreenState currentScreen = ScreenState.MAIN_MENU;
    private Language  currentLanguage = Language.ENGLISH;

    public void setScreen(ScreenState currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void setLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
}
