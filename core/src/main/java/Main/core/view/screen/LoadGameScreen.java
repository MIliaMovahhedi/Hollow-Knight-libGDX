package Main.core.view.screen;

import Main.core.controller.StartGameController;
import Main.core.controller.manager.SaveManager;
import Main.core.model.SaveData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class LoadGameScreen extends MenuScreen {

    private StartGameController controller;
    private Table slotsTable;
    private TextButton backButton;

    public LoadGameScreen() {
        controller = new StartGameController();
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.top().padTop(50);

        Label titleLabel = new Label("SELECT PROFILE", skin, "title");
        table.add(titleLabel).padBottom(10).row();

        Image warningFleur = new Image(skin.getDrawable("Warning_Fleur"));
        table.add(warningFleur).padBottom(40).row();

        slotsTable = new Table();
        refreshSlots();
        table.add(slotsTable).row();

        backButton = new TextButton("BACK", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToMainMenu();
            }
        });
        table.add(backButton).padTop(30);
        stage.addActor(table);
    }

    private void refreshSlots() {
        slotsTable.clearChildren();
        for (int i = 1; i <= 4; i++) {
            slotsTable.add(createSlot(i)).padBottom(10).row();
        }
    }

    private Table createSlot(final int slotIndex) {
        Table mainContainer = new Table();

        Stack slotStack = new Stack();

        Table fleurLayer = new Table();
        Image topFleur = new Image(skin.getDrawable("profile_fleur"));
        fleurLayer.add(topFleur).width(800).top().left();
        slotStack.add(fleurLayer);

        Table contentLayer = new Table();
        contentLayer.top().left().padTop(30);

        SaveData data = SaveManager.getInstance().loadGame(slotIndex);

        Label clearButton = new Label("CLEAR SAVE", skin, "title");
        clearButton.setFontScale(0.5f);
        clearButton.setVisible(data != null);

        if (data == null) {
            Label numberLabel = new Label(slotIndex + ". ", skin, "title");
            numberLabel.setFontScale(0.8f);

            Label newGameLabel = new Label("NEW GAME", skin, "title");
            newGameLabel.setFontScale(0.6f);

            contentLayer.add(numberLabel).width(50).left().padLeft(30);
            contentLayer.add(newGameLabel).left().padLeft(10).expandX();
        } else {
            Label numberLabel = new Label(slotIndex + ". ", skin, "title");
            numberLabel.setFontScale(0.8f);

            Stack mapBoxStack = new Stack();
            String bgName = (data.getMapIndex() == 1 || data.getMapIndex() == 2) ? "Area_Green_Path" : "Area_Forgotten Crossroads";
            Image bgImage = new Image(skin.getDrawable(bgName));
            mapBoxStack.add(bgImage);

            Table infoTable = new Table();
            String mapName = (data.getMapIndex() == 1 || data.getMapIndex() == 2) ? "GREENPATH" : "FORGOTTEN CROSSROADS";
            Label mapNameLabel = new Label(mapName, skin, "title");
            mapNameLabel.setFontScale(0.4f);

            int totalSeconds = (int) data.getGameStats().getPlayTime();
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            Label timeLabel = new Label(hours + "H " + minutes + "M", skin, "desc");
            timeLabel.setFontScale(0.4f);

            infoTable.add(mapNameLabel).right().expandX().padRight(20).padTop(5).row();
            infoTable.add(timeLabel).right().expandX().padRight(20).padBottom(5);

            mapBoxStack.add(infoTable);

            contentLayer.add(numberLabel).width(50).left().padLeft(30);
            contentLayer.add(mapBoxStack).width(500).height(65).left();
            contentLayer.add().expandX();
        }

        contentLayer.add(clearButton).width(120).right().padRight(20);

        slotStack.add(contentLayer);

        mainContainer.add(slotStack).width(800).height(100);

        contentLayer.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                contentLayer.getColor().a = 0.7f;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                contentLayer.getColor().a = 1.0f;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == clearButton) {
                    return;
                }
                if (data == null) {
                    controller.startNewGame(slotIndex);
                } else {
                    controller.loadGame(slotIndex);
                }
            }
        });

        clearButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                clearButton.setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                clearButton.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                SaveManager.getInstance().deleteSlot(slotIndex);
                refreshSlots();
            }
        });

        return mainContainer;
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (backButton != null && bundle != null) {
            backButton.setText(bundle.get("menu.back"));
        }
    }
}
