package Main.core.view.screen;

import Main.core.controller.GuideController;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class GuideMenuScreen extends MenuScreen {

    private final GuideController controller;
    private TextButton backButton;

    public GuideMenuScreen() {
        controller = new GuideController();
    }

    @Override
    protected void addButtonsAndListeners() {
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToMainMenu();
            }
        });
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.center();

        Label title = new Label("GUIDE", skin, "title");
        title.setFontScale(1.2f);
        table.add(title).padBottom(30).row();

        Table contentTable = new Table();
        contentTable.left();

        addGuideSection(contentTable, "Game Controls:",
            "Move: Arrow Keys | Jump/Double Jump: Z | Dash: C\n" +
                "Attack: X | Focus (Heal): Hold A | Spell: S\n" +
                "Inventory: I | Pause Game: ESC");

        addGuideSection(contentTable, "Knight Abilities & Attributes:",
            "- Health System: Represented by Masks. Replenish lost masks by Focusing.\n" +
                "- Soul Vessel: Earn SOUL by striking enemies with your Nail.\n" +
                "- Use SOUL to cast powerful Spells (S) or Heal yourself (Hold A).");

        addGuideSection(contentTable, "Cheat Codes (Hold Left Ctrl + Key):",
            "Ctrl + G : Toggle God Mode (Invincibility)\n" +
                "Ctrl + N : Toggle Noclip (Fly through walls)\n" +
                "Ctrl + H : Maximize Health & Soul Vessel\n" +
                "Ctrl + E : Toggle Emergency Heal (Auto-Revive on death)\n" + // --- FIXED ---
                "Ctrl + K : Insta-Kill all enemies in the current room\n" +
                "Ctrl + B : Teleport directly to the False Knight Boss Arena");

        table.add(contentTable).padBottom(40).row();
        table.add(backButton).uniformX().center();

        stage.addActor(table);
    }

    private void addGuideSection(Table parentTable, String titleText, String descText) {
        Label titleLabel = new Label(titleText, skin, "title");
        titleLabel.setFontScale(0.65f);
        titleLabel.setColor(Color.GOLD);

        Label descLabel = new Label(descText, skin, "desc");
        descLabel.setFontScale(0.6f);
        descLabel.setWrap(true);

        parentTable.add(titleLabel).left().padBottom(5).row();
        parentTable.add(descLabel).width(700).left().padBottom(25).row();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        if (backButton != null && bundle != null) {
            backButton.setText(bundle.get("menu.back"));
        }
    }
}
