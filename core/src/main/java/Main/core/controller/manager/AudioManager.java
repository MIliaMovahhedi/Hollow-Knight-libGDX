package Main.core.controller.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private static AudioManager instance;

    private float musicVolume;
    private float sfxVolume;

    private Music currentMusic;
    private Music nextMusic;
    private float fadeDuration;
    private float fadeTimer;
    private boolean isFading;

    public final Sound slashSound;
    public final Sound dashSound;
    public final Sound runSound;
    public final Sound pogoSound;
    public final Sound jumpSound;
    public final Sound fallSound;
    public final Sound focusReadySound;
    public final Sound focusSound;
    public final Sound focusGetSound;
    public final Sound screamSpellSound;
    public final Sound soulCollectSound;
    public final Sound knightDamageSound;
    public final Sound deathSound;
    public final Sound mossflyFlySound;
    public final Sound mossflyEmergeSound;
    public final Sound crawlidCrawlSound;
    public final Sound guardianRunSound;
    public final Sound huskAttackSound;
    public final Sound enemyDamageSound;
    public final Sound achivementPopUp;
    public final Sound[] zoteTalkingSounds;
    public final Sound[] zoteMumbleSounds;

    public final Music crossroadsMusic;
    public final Music greenpathMusic;
    public final Music menuMusic;

    private float preMuteMusicVolume = 1.0f;
    private float preMuteSfxVolume = 1.0f;
    private boolean isMusicMuted = false;
    private boolean isSfxMuted = false;

    private AudioManager() {

        this.musicVolume = 1.0f;
        this.sfxVolume = 1.0f;
        this.isFading = false;

        this.slashSound = Gdx.audio.newSound(Gdx.files.internal("voices/sword_1.wav"));
        this.dashSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_dash.wav"));
        this.runSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_run_footsteps_stone.wav"));
        this.pogoSound = Gdx.audio.newSound(Gdx.files.internal("voices/sword_hit_reject.wav"));
        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_jump.wav"));
        this.fallSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_falling.wav"));
        this.focusReadySound = Gdx.audio.newSound(Gdx.files.internal("voices/focus_ready.wav"));
        this.focusSound = Gdx.audio.newSound(Gdx.files.internal("voices/focus_health_charging.wav"));
        this.focusGetSound = Gdx.audio.newSound(Gdx.files.internal("voices/focus_health_heal.wav"));
        this.screamSpellSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_scream_spell.wav"));
        this.soulCollectSound = Gdx.audio.newSound(Gdx.files.internal("voices/soul_pickup_1.wav"));
        this.knightDamageSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_damage.wav"));
        this.deathSound = Gdx.audio.newSound(Gdx.files.internal("voices/hero_death_v2.wav"));
        this.mossflyFlySound = Gdx.audio.newSound(Gdx.files.internal("voices/fly_flying.wav"));
        this.mossflyEmergeSound = Gdx.audio.newSound(Gdx.files.internal("voices/moss_flyer_walker_emerge.wav"));
        this.crawlidCrawlSound = Gdx.audio.newSound(Gdx.files.internal("voices/crawler.wav"));
        this.guardianRunSound = Gdx.audio.newSound(Gdx.files.internal("voices/zombie_laser_miner_run.wav"));
        this.huskAttackSound = Gdx.audio.newSound(Gdx.files.internal("voices/zombie_laser_miner_run.wav"));
        this.enemyDamageSound = Gdx.audio.newSound(Gdx.files.internal("voices/enemy_damage.wav"));
        this.achivementPopUp = Gdx.audio.newSound(Gdx.files.internal("voices/achievementPopup.wav"));

        this.zoteTalkingSounds = new Sound[]{
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_01.wav")),
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_02.wav")),
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_03.wav")),
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_04.wav")),
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_05.wav"))
        };

        this.zoteMumbleSounds = new Sound[]{
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_Balloon_idle_02.wav")),
            Gdx.audio.newSound(Gdx.files.internal("voices/Zote_Balloon_idle_03.wav"))
        };

        this.crossroadsMusic = Gdx.audio.newMusic(Gdx.files.internal("voices/S19 Crossroads Main.wav"));
        this.greenpathMusic = Gdx.audio.newMusic(Gdx.files.internal("voices/S5 Green Path Main.wav"));
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("voices/Hollow Knight.mp3"));
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void update(float deltaTime) {
        if (!isFading) {
            if (currentMusic != null && currentMusic.isPlaying()) {
                currentMusic.setVolume(musicVolume);
            }
            return;
        }

        fadeTimer += deltaTime;
        float progress = Math.min(fadeTimer / fadeDuration, 1.0f);

        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume * (1.0f - progress));
        }

        if (nextMusic != null) {
            if (!nextMusic.isPlaying()) {
                nextMusic.play();
            }
            nextMusic.setVolume(musicVolume * progress);
        }

        if (progress >= 1.0f) {
            isFading = false;
            if (currentMusic != null) {
                currentMusic.stop();
            }
            currentMusic = nextMusic;
            nextMusic = null;
        }
    }

    public void toggleMusicMute() {
        isMusicMuted = !isMusicMuted;
        if (isMusicMuted) {
            preMuteMusicVolume = musicVolume;
            setMusicVolume(0f);
        } else {
            setMusicVolume(preMuteMusicVolume > 0f ? preMuteMusicVolume : 1.0f);
        }
    }

    public void toggleSfxMute() {
        isSfxMuted = !isSfxMuted;
        if (isSfxMuted) {
            preMuteSfxVolume = sfxVolume;
            setSfxVolume(0f);
        } else {
            setSfxVolume(preMuteSfxVolume > 0f ? preMuteSfxVolume : 1.0f);
        }
    }

    public void resetAudioSettings() {
        isMusicMuted = false;
        isSfxMuted = false;
        setMusicVolume(1.0f);
        setSfxVolume(1.0f);
    }

    public void playSfx(Sound sound) {
        if (sound != null) {
            sound.play(sfxVolume);
        }
    }

    public void loopSfx(Sound sound) {

        if (sound != null) {
            sound.loop(sfxVolume);
        }
    }

    public void stopSfx(Sound sound) {

        if (sound != null) {
            sound.stop();
        }
    }

    public void playMusic(Music music, boolean loop) {
        if (currentMusic == music) {
            return;
        }

        if (currentMusic != null) {
            currentMusic.stop();
        }

        currentMusic = music;
        if (currentMusic != null) {
            currentMusic.setLooping(loop);
            currentMusic.setVolume(musicVolume);
            currentMusic.play();
        }
    }

    public void crossfadeMusic(Music newMusic, float duration, boolean loop) {
        if (currentMusic == newMusic) {
            return;
        }

        nextMusic = newMusic;
        if (nextMusic != null) {
            nextMusic.setLooping(loop);
            nextMusic.setVolume(0f);
        }

        fadeDuration = duration;
        fadeTimer = 0f;
        isFading = true;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0f, Math.min(volume, 1.0f));

        if (currentMusic != null && !isFading) {
            currentMusic.setVolume(this.musicVolume);
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0f, Math.min(volume, 1.0f));
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void stopAll() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
        if (nextMusic != null) {
            nextMusic.stop();
            nextMusic = null;
        }
        isFading = false;
    }

    public void playScaledSfx(Sound sound, float volumeScale) {

        if (sound != null) {
            sound.play(sfxVolume * volumeScale);
        }
    }

    public void playSpatialSfx(Sound sound, float distance, float maxHearingDistance) {

        if (sound != null) {
            float distanceRatio = Math.max(0.0f, 1.0f - (distance / maxHearingDistance));
            float finalVolume = sfxVolume * distanceRatio;
            if (finalVolume > 0.0f) {
                sound.play(finalVolume);
            }
        }
    }

    public long loopSpatialSfx(Sound sound, float distance, float maxHearingDistance) {

        if (sound != null) {
            float distanceRatio = Math.max(0.0f, 1.0f - (distance / maxHearingDistance / 2));
            float finalVolume = sfxVolume * distanceRatio;
            if (finalVolume > 0.0f) {
                return sound.loop(finalVolume);
            } else {
                return sound.loop(0.0f);
            }
        }
        return -1;
    }

    public void updateLoopVolume(Sound sound, long soundId, float distance, float maxHearingDistance) {

        if (sound != null && soundId != -1) {
            float distanceRatio = Math.max(0.0f, 1.0f - (distance / maxHearingDistance / 2));
            float finalVolume = sfxVolume * distanceRatio;
            sound.setVolume(soundId, finalVolume);
        }
    }
}
