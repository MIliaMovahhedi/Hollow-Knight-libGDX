package Main.core.view.screen;

import Main.core.controller.PauseMenuController;
import Main.core.model.enums.ScreenState;
import Main.core.model.knight.KnightModel;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class PauseMenuScreen extends MenuScreen {

    private final PauseMenuController controller;
    private TextButton resumeButton;
    private TextButton settingsButton;
    private TextButton mainMenuButton;
    private TextButton saveAndQuitButton;
    private Label activeCheatsLabel;

    public PauseMenuScreen() {
        controller = new PauseMenuController();
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
    }

    @Override
    protected void addButtonsAndListeners() {
        resumeButton = new TextButton("Resume", skin);
        TextButton inventoryButton = new TextButton("Inventory", skin);
        settingsButton = new TextButton("Settings", skin);
        saveAndQuitButton = new TextButton("Save & Quit", skin);
        mainMenuButton = new TextButton("Main Menu", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { controller.resumeGame(); }
        });

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { ScreenManager.getInstance().navigateTo(ScreenState.INVENTORY_MENU); }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { controller.goToSettings(); }
        });

        saveAndQuitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { controller.saveAndQuitGame(); }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { controller.goToMainMenu(); }
        });

        Table leftColumn = new Table();
        leftColumn.add(resumeButton).spaceBottom(15).uniformX().row();
        leftColumn.add(inventoryButton).spaceBottom(15).uniformX().row();
        leftColumn.add(settingsButton).spaceBottom(15).uniformX().row();
        leftColumn.add(saveAndQuitButton).spaceBottom(15).uniformX().row();
        leftColumn.add(mainMenuButton).spaceBottom(15).uniformX().row();

        Table rightColumn = new Table();
        Label cheatTitle = new Label("Cheat Codes Reference:", skin, "title");
        cheatTitle.setFontScale(0.7f);
        cheatTitle.setColor(Color.GOLD);

        String cheatText =
            "Hold Left Ctrl +\n" +
                "[ G ] : Toggle God Mode\n" +
                "[ N ] : Toggle Noclip (Fly)\n" +
                "[ H ] : Max Health & Soul\n" +
                "[ E ] : Toggle Auto-Revive\n" +
                "[ K ] : Kill All Enemies\n" +
                "[ B ] : Teleport to Boss";

        Label cheatGuideLabel = new Label(cheatText, skin, "desc");
        cheatGuideLabel.setFontScale(0.65f);

        activeCheatsLabel = new Label("Active: None", skin, "desc");
        activeCheatsLabel.setColor(Color.YELLOW);
        activeCheatsLabel.setFontScale(0.75f);

        rightColumn.add(cheatTitle).left().padBottom(10).row();
        rightColumn.add(cheatGuideLabel).left().padBottom(20).row();
        rightColumn.add(activeCheatsLabel).left().row();

        table.add(leftColumn).padRight(100);
        table.add(rightColumn).left();
    }

    @Override
    public void render(float deltaSeconds) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            controller.resumeGame();
            return;
        }

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.renderGameGraphics(0);
            if (gameScreen.getKnightModel() != null) {
                KnightModel knight = gameScreen.getKnightModel();
                String cheats = "";
                if (knight.isGodMode()) cheats += "[GodMode] ";
                if (knight.isNoclip()) cheats += "[NoClip] ";
                if (knight.hasEmergencyHeal()) cheats += "[Auto-Revive] ";

                if (cheats.isEmpty()) activeCheatsLabel.setText("Active Cheats: None");
                else activeCheatsLabel.setText("Active Cheats: \n" + cheats);
            }
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        }

        stage.act(deltaSeconds);
        stage.draw();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (resumeButton != null && bundle != null) {
            resumeButton.setText(bundle.get("menu.resume"));
            settingsButton.setText(bundle.get("menu.settings"));
            mainMenuButton.setText(bundle.get("menu.mainMenu"));
        }
    }
}
