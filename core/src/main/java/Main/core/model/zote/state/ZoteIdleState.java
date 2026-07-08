package Main.core.model.zote.state;

import Main.core.controller.manager.AudioManager;
import Main.core.model.zote.ZoteModel;
import com.badlogic.gdx.math.MathUtils;

public class ZoteIdleState implements ZoteState {

    private int currentMumbleIndex;

    private long currentSoundId;

    private final float MAX_HEARING_DISTANCE = 800.0f;

    @Override
    public void enter(ZoteModel zote) {

        currentMumbleIndex = MathUtils.random(0, 1);

        currentSoundId = AudioManager.getInstance().loopSpatialSfx(
            AudioManager.getInstance().zoteMumbleSounds[currentMumbleIndex],
            zote.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );
    }

    @Override
    public void update(ZoteModel zote, float deltaTime) {

        AudioManager.getInstance().updateLoopVolume(
            AudioManager.getInstance().zoteMumbleSounds[currentMumbleIndex],
            currentSoundId,
            zote.getDistanceToPlayer(),
            MAX_HEARING_DISTANCE
        );
    }

    @Override
    public void interact(ZoteModel zote) {
        zote.changeState(new ZoteTalkingState());
    }

    @Override
    public void exit(ZoteModel zote) {

        AudioManager.getInstance().stopSfx(AudioManager.getInstance().zoteMumbleSounds[currentMumbleIndex]);
    }
}
