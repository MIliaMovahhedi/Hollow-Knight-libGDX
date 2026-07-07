package Main.core.view.screen;

import Main.core.controller.GuideController;
import Main.core.controller.InventoryController;
import Main.core.model.charm.Charm;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class InventoryMenuScreen extends MenuScreen {

    private InventoryController controller;

    private Label detailNameLabel;
    private Label detailDescLabel;
    private Image detailCharmImage;
    private Table notchesTable;
    private Table equippedCharmsTable;

    private Label notchesHeaderLabel;
    private Label noNotchErrorLabel;
    private Color originalGrayColor;

    public InventoryMenuScreen() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/hollowNightSkin.json"));
        controller = new InventoryController();

        buildUI();
    }

    private void buildUI() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);

        Table bgTable = new Table();
        bgTable.setBackground(skin.get("default", com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle.class).background);

        com.badlogic.gdx.graphics.g2d.TextureRegion cornerBase = skin.getRegion("overscan_corner_fleur009");

        com.badlogic.gdx.graphics.g2d.TextureRegion trFlipX = new com.badlogic.gdx.graphics.g2d.TextureRegion(cornerBase);
        trFlipX.flip(true, false);

        com.badlogic.gdx.graphics.g2d.TextureRegion trFlipY = new com.badlogic.gdx.graphics.g2d.TextureRegion(cornerBase);
        trFlipY.flip(false, true);

        com.badlogic.gdx.graphics.g2d.TextureRegion trFlipXY = new com.badlogic.gdx.graphics.g2d.TextureRegion(cornerBase);
        trFlipXY.flip(true, true);

        bgTable.add(new Image(cornerBase)).top().left().expand();
        bgTable.add(new Image(trFlipX)).top().right().expand().row();
        bgTable.add(new Image(trFlipY)).bottom().left().expand();
        bgTable.add(new Image(trFlipXY)).bottom().right().expand();

        Table uiTable = new Table();
        uiTable.setFillParent(true);

        uiTable.add(new Image(skin.getDrawable("charmScreen_border_top"))).colspan(2).padTop(10).row();
        uiTable.add(new Label("Charms", skin, "title")).colspan(2).padBottom(20).row();

        Table leftPane = new Table();
        Table rightPane = new Table();

        rightPane.top().padTop(20);

        detailNameLabel = new Label("Select a Charm", skin, "title");
        detailNameLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        detailNameLabel.setWrap(true);
        detailNameLabel.setFontScale(0.85f);

        Table costTable = new Table();
        Label costLabel = new Label("Cost  ", skin, "desc");
        costLabel.setFontScale(0.8f);
        costTable.add(costLabel);
        costTable.add(new Image(skin.getDrawable("charm_UI__0001_charm_cost_02_unlit"))).size(18);

        detailCharmImage = new Image();

        detailDescLabel = new Label("", skin, "desc");
        detailDescLabel.setWrap(true);
        detailDescLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        detailDescLabel.setFontScale(0.70f);

        rightPane.add(detailNameLabel).width(350).minHeight(60).padBottom(5).row();
        rightPane.add(costTable).padBottom(15).row();
        rightPane.add(detailCharmImage).size(100, 100).padBottom(20).row();
        rightPane.add(detailDescLabel).width(350).top().row();

        equippedCharmsTable = new Table();
        leftPane.add(equippedCharmsTable).left().padBottom(15).height(70).row();

        notchesHeaderLabel = new Label("Notches", skin, "desc");
        originalGrayColor = new Color(notchesHeaderLabel.getColor());

        Table notchHeaderTable = new Table();
        notchHeaderTable.add(notchesHeaderLabel).padRight(15);
        notchesTable = new Table();
        notchHeaderTable.add(notchesTable);
        leftPane.add(notchHeaderTable).left().padBottom(5).row();

        noNotchErrorLabel = new Label("Not Enough Notches!", skin, "desc");
        noNotchErrorLabel.setColor(Color.RED);
        noNotchErrorLabel.getColor().a = 0f;
        leftPane.add(noNotchErrorLabel).left().padBottom(15).row();

        leftPane.add(new Image(skin.getDrawable("Inv_0017_divider"))).fillX().padBottom(20).row();

        Table grid = new Table();
        String[] allCharms = {
            "Dashmaster - _0011_charm_generic_03",
            "Quick Focus - _0005_charm_fast_focus",
            "Quick Slash - _0003_charm_nail_slash_speed_up",
            "Soul Catcher - _0001_charm_more_soul",
            "Unbreakable Strength_0002_charm_glass_attack_up_full"
        };
        int columns = 0;

        for (String charmName : allCharms) {
            ImageButton charmBtn = new ImageButton(skin.getDrawable(charmName));

            charmBtn.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    updateRightPane(charmName);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (controller.toggleCharm(charmName)) {
                        updateLeftPaneDynamically();
                    } else {
                        showErrorAnimation();
                    }
                }
            });

            grid.add(charmBtn).size(70, 70).pad(8);
            columns++;
            if (columns % 6 == 0) grid.row();
        }
        leftPane.add(grid).left();

        uiTable.add(leftPane).expand().top().left().pad(40);
        uiTable.add(rightPane).width(400).top().right().pad(40).row();
        uiTable.add(new Image(skin.getDrawable("bottom_fleur0008"))).colspan(2).padBottom(10);

        rootStack.add(bgTable);
        rootStack.add(uiTable);

        stage.addActor(rootStack);

        updateLeftPaneDynamically();
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        Main.core.model.LocalizationManager.getInstance().register(this);

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null && gameScreen.getKnightModel() != null) {

            controller.setKnightModel(gameScreen.getKnightModel());
        }
        updateLeftPaneDynamically();
    }

    private void showErrorAnimation() {

        notchesHeaderLabel.clearActions();
        notchesHeaderLabel.addAction(Actions.sequence(
            Actions.color(Color.RED, 0.1f),
            Actions.delay(0.1f),
            Actions.color(originalGrayColor, 0.4f)
        ));

        noNotchErrorLabel.clearActions();
        noNotchErrorLabel.addAction(Actions.sequence(
            Actions.alpha(1f, 0.1f),
            Actions.delay(1.5f),
            Actions.alpha(0f, 1f)
        ));
    }

    private void updateRightPane(String charmId) {

        detailNameLabel.setText(controller.getDisplayName(charmId));
        detailDescLabel.setText(controller.getDescription(charmId));
        detailCharmImage.setDrawable(skin.getDrawable(charmId));
    }

    private void updateLeftPaneDynamically() {

        equippedCharmsTable.clear();
        for (String equippedCharm : controller.getEquippedCharms()) {
            equippedCharmsTable.add(new Image(skin.getDrawable(equippedCharm))).size(60, 60).pad(5);
        }

        notchesTable.clear();
        int used = controller.getUsedNotches();
        for (int i = 0; i < controller.getMaxNotches(); i++) {

            if (i < used) {

                notchesTable.add(new Image(skin.getDrawable("charm_UI__0000_charm_cost_02_lit"))).pad(3);
            }
            else {

                notchesTable.add(new Image(skin.getDrawable("charm_UI__0001_charm_cost_02_unlit"))).pad(3);
            }
        }
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.I) ||
            Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {

            GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
            if (gameScreen != null) {

                gameScreen.setPaused(false);
            }
            ScreenManager.getInstance().navigateTo(ScreenState.GAME_MENU);
            return;
        }

        GameScreen gameScreen = (GameScreen) ScreenManager.getInstance().getCachedScreen(ScreenState.GAME_MENU);
        if (gameScreen != null) {
            gameScreen.renderGameGraphics(0);
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }
}
