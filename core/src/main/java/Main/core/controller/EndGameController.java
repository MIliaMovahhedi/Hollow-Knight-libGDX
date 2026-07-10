package Main.core.controller;

import Main.core.model.GameState;
import Main.core.model.enums.ScreenState;

public class EndGameController {

    public void returnToMainMenu() {

        GameState.getInstance().setScreen(ScreenState.MAIN_MENU);
        GameState.getInstance().resetGameStats();
    }
}
