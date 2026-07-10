package Main.core.view;

import Main.core.controller.MainMenuController;
import Main.core.model.enums.ScreenState;
import Main.core.view.screen.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;

public class ScreenManager {

    private static ScreenManager instance;
    private HashMap<ScreenState, Screen> screens = new HashMap<>();
    private Game game;

    private ScreenManager(){}
    public static ScreenManager getInstance(){
        if(instance == null){
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Game game){
        this.game = game;
    }

    public void navigateTo(ScreenState screenState) {

        if (!screens.containsKey(screenState)) {

            screens.put(screenState, getScreen(screenState));
        }

        game.setScreen(screens.get(screenState));
    }

    private Screen getScreen(ScreenState screenState) {

        return switch (screenState) {

            case MAIN_MENU ->  new MainMenuScreen();
            case SETTINGS_MENU ->   new SettingsMenuScreen();
            case START_GAME_MENU ->   new StartGameMenuScreen();
            case GUIDE_MENU ->    new GuideMenuScreen();
            case PAUSE_MENU ->     new PauseMenuScreen();
            case INVENTORY_MENU ->     new InventoryMenuScreen();
            case ACHIEVEMENTS_MENU ->     new AchievementsMenuScreen();
            case GAME_MENU -> new GameScreen();
            case END_GAME -> new EndGameScreen();
        };
    }

    public Screen getCachedScreen(ScreenState screenState) {

        return screens.get(screenState);
    }
}
