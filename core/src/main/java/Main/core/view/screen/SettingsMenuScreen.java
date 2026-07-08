package Main.core.view.screen;

import Main.core.controller.SettingController;
import Main.core.controller.manager.AudioManager;
import Main.core.view.ScreenManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class SettingsMenuScreen extends MenuScreen {

    private final SettingController controller;
    private TextButton changeLanguageButton;
    private TextButton backButton;
    private TextButton muteMusicButton;
    private TextButton muteSfxButton;
    private TextButton resetAudioButton;
    private Slider musicSlider;
    private Slider sfxSlider;
    private Label musicLabel;
    private Label sfxLabel;

    public SettingsMenuScreen() {
        controller = new SettingController();
    }

    @Override
    protected void createTableLayout() {
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("main_menu_bg"));
        table.center();
        stage.addActor(table);
    }

    @Override
    protected void addButtonsAndListeners() {
        musicLabel = new Label("Music Volume", skin);
        sfxLabel = new Label("SFX Volume", skin);

        musicSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicSlider.setValue(AudioManager.getInstance().getMusicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().setMusicVolume(musicSlider.getValue());
            }
        });

        sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);
        sfxSlider.setValue(AudioManager.getInstance().getSfxVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().setSfxVolume(sfxSlider.getValue());
                if (!sfxSlider.isDragging()) {
                    AudioManager.getInstance().playSfx(AudioManager.getInstance().slashSound);
                }
            }
        });

        muteMusicButton = new TextButton("Mute", skin);
        muteMusicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                AudioManager.getInstance().toggleMusicMute();
                musicSlider.setValue(AudioManager.getInstance().getMusicVolume());
            }
        });

        muteSfxButton = new TextButton("Mute", skin);
        muteSfxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                AudioManager.getInstance().toggleSfxMute();
                sfxSlider.setValue(AudioManager.getInstance().getSfxVolume());
            }
        });

        resetAudioButton = new TextButton("Reset Audio", skin);
        resetAudioButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                AudioManager.getInstance().resetAudioSettings();
                musicSlider.setValue(AudioManager.getInstance().getMusicVolume());
                sfxSlider.setValue(AudioManager.getInstance().getSfxVolume());
            }
        });

        changeLanguageButton = new TextButton("Change Language", skin);
        changeLanguageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.changeLanguage();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float coordinateX, float coordinateY) {
                controller.goBackToMainMenu();
            }
        });

        table.add(musicLabel).colspan(2).spaceBottom(10).row();
        table.add(musicSlider).width(300).spaceBottom(20);
        table.add(muteMusicButton).spaceBottom(20).padLeft(10).row();

        table.add(sfxLabel).colspan(2).spaceBottom(10).row();
        table.add(sfxSlider).width(300).spaceBottom(30);
        table.add(muteSfxButton).spaceBottom(30).padLeft(10).row();

        table.add(resetAudioButton).colspan(2).spaceBottom(15).uniformX().fillX().row();
        table.add(changeLanguageButton).colspan(2).spaceBottom(15).uniformX().fillX().row();
        table.add(backButton).colspan(2).spaceBottom(15).uniformX().fillX().row();
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
        changeLanguageButton.setText(bundle.get("menu.changeLanguage"));
        backButton.setText(bundle.get("menu.back"));
    }
}
