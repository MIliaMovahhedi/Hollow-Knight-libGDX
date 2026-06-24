package Main.core.controller;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MainMenuController {

    public void goToSettings() {

        ScreenManager.getInstance().navigateTo(ScreenState.SETTINGS_MENU);
    }
    public void goToStartGameMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.START_GAME_MENU);
    }
    public void goToAchievementMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.ACHIEVEMENTS_MENU);
    }
    public void goToGuideMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.GUIDE_MENU);
    }
    public void goToPauseMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.PAUSE_MENU);
    }
    public void goToInventoryMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.INVENTORY_MENU);
    }
    public void quitGame() {

        Gdx.app.exit();
    }

}
