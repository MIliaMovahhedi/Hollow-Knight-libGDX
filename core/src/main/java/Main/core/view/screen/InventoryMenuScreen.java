package Main.core.view.screen;

import Main.core.controller.GuideController;
import Main.core.controller.InventoryController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InventoryMenuScreen extends MenuScreen {

    private final InventoryController controller;
    private TextButton backButton;

    public InventoryMenuScreen() {
        controller = new InventoryController();
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

        table.add(backButton).spaceBottom(15).uniformX();
    }
}
