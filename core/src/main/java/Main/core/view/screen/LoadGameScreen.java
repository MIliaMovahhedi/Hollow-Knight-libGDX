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
            slotsTable.add(createSlot(i)).padBottom(15).row();
        }
    }

    private Table createSlot(final int slotIndex) {
        Table rowTable = new Table();

        Image topFleur = new Image(skin.getDrawable("profile_fleur"));
        rowTable.add(topFleur).colspan(2).width(800).left().padBottom(5).row();

        Table slotContent = new Table();
        SaveData data = SaveManager.getInstance().loadGame(slotIndex);

        Label clearButton = new Label("CLEAR SAVE", skin, "title");
        clearButton.setFontScale(0.5f);
        clearButton.setVisible(data != null);

        if (data == null) {
            Label newGameLabel = new Label(slotIndex + ".    NEW GAME", skin, "title");
            newGameLabel.setFontScale(0.8f);
            slotContent.add(newGameLabel).left().expandX().padLeft(20);
        } else {

            String bgName = data.getMapIndex() == 1 || data.getMapIndex() == 2 ? "Area_Green_Path" : "Area_Forgotten Crossroads";
            slotContent.setBackground(skin.getDrawable(bgName));

            Label slotNumberLabel = new Label(slotIndex + ". ", skin, "title");
            slotNumberLabel.setFontScale(0.8f);

            Table infoTable = new Table();
            String mapName = data.getMapIndex() == 1 || data.getMapIndex() == 2 ? "GREENPATH" : "FORGOTTEN CROSSROADS";
            Label mapNameLabel = new Label(mapName, skin, "title");
            mapNameLabel.setFontScale(0.6f);

            int totalSeconds = (int) data.getGameStats().getPlayTime();
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            Label timeLabel = new Label(hours + "H " + minutes + "M", skin, "desc");
            timeLabel.setFontScale(0.6f);

            infoTable.add(mapNameLabel).right().expandX().padRight(20).padTop(5).row();
            infoTable.add(timeLabel).right().expandX().padRight(20).padBottom(5);

            slotContent.add(slotNumberLabel).width(60).left().padLeft(15);
            slotContent.add(infoTable).right().expandX();
        }

        rowTable.add(slotContent).width(660).height(65).left().padLeft(15);
        rowTable.add(clearButton).width(120).center();

        slotContent.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                slotContent.getColor().a = 0.7f;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                slotContent.getColor().a = 1.0f;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
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

        return rowTable;
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (backButton != null && bundle != null) {
            backButton.setText(bundle.get("menu.back"));
        }
    }
}
