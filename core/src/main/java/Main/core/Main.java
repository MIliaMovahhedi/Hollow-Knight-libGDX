package Main.core;

import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {

        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
}
