package Main.core.view.screen;

import Main.core.controller.StartGameController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class StartGameMenuScreen extends MenuScreen {

    private final StartGameController controller;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton backButton;

    public StartGameMenuScreen() {
        controller = new StartGameController();
    }

    @Override
    protected void addButtonsAndListeners() {
        newGameButton = new TextButton("New Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        backButton = new TextButton("Back", skin);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.startNewGame();
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.loadGame();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToMainMenu();
            }
        });

        table.add(newGameButton).spaceBottom(15).uniformX();
        table.row();
        table.add(loadGameButton).spaceBottom(15).uniformX();
        table.row();
        table.add(backButton).spaceBottom(15).uniformX();
    }
}
