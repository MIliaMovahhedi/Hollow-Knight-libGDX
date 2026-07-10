package Main.core.view.renderer;

import Main.core.model.LevelModel;
import Main.core.model.enemy.*;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.EnemyTurnState;
import Main.core.model.enemy.state.crystalGuardianState.GuardianEnragedState;
import Main.core.model.enemy.state.crystalGuardianState.GuardianIdleState;
import Main.core.model.enemy.state.crystalGuardianState.GuardianShootState;
import Main.core.model.enemy.state.huskHornHeadState.HuskChargeState;
import Main.core.model.enemy.state.huskHornHeadState.HuskPatrolState;
import Main.core.model.enemy.state.huskHornHeadState.HuskRestState;
import Main.core.model.enemy.state.mossflyState.MossflyChargeState;
import Main.core.model.enemy.state.mossflyState.MossflyEmergeState;
import Main.core.model.enemy.state.mossflyState.MossflyHiddenState;
import Main.core.model.enemy.state.mossflyState.MossflyHoverState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class EnemyRenderer {

    private static class EnemyViewData {

        float stateTime = 0.0f;
        boolean wasDead = false;
        Class<? extends EnemyState> previousStateType = null;
    }

    private final Map<EnemyModel, EnemyViewData> viewDataMap;

    private final Animation<TextureRegion> crawlidWalk;
    private final Animation<TextureRegion> crawlidTurn;
    private final Animation<TextureRegion> crawlidDeathAir;
    private final Animation<TextureRegion> crawlidDeathLand;

    private final Animation<TextureRegion> mossflyShake;
    private final Animation<TextureRegion> mossflyAppear;
    private final Animation<TextureRegion> mossflyTurnToFly;
    private final Animation<TextureRegion> mossflyFly;
    private final Animation<TextureRegion> mossflyDeathAir;
    private final Animation<TextureRegion> mossflyDeathLand;

    private final Animation<TextureRegion> huskIdle;
    private final Animation<TextureRegion> huskWalk;
    private final Animation<TextureRegion> huskAnticipate;
    private final Animation<TextureRegion> huskLunge;
    private final Animation<TextureRegion> huskCooldown;
    private final Animation<TextureRegion> huskTurn;
    private final Animation<TextureRegion> huskDeathAir;
    private final Animation<TextureRegion> huskDeathLand;

    private final Animation<TextureRegion> guardianIdle;
    private final Animation<TextureRegion> guardianRun;
    private final Animation<TextureRegion> guardianShoot;
    private final Animation<TextureRegion> guardianTurn;
    private final Animation<TextureRegion> guardianDeathAir;
    private final Animation<TextureRegion> guardianDeathLand;
    private final TextureRegion laserBeamRegion;
    private final Animation<TextureRegion> laserOrbAnim;

    public EnemyRenderer(TextureAtlas crawlidAtlas, TextureAtlas mossflyAtlas, TextureAtlas huskAtlas, TextureAtlas guardianAtlas) {
        this.viewDataMap = new HashMap<>();

        crawlidWalk = new Animation<>(0.1f, crawlidAtlas.findRegions("Walk"), Animation.PlayMode.LOOP);
        crawlidTurn = new Animation<>(0.1f, crawlidAtlas.findRegions("turn"), Animation.PlayMode.NORMAL);
        crawlidDeathAir = new Animation<>(0.1f, crawlidAtlas.findRegions("Death Air"), Animation.PlayMode.LOOP);
        crawlidDeathLand = new Animation<>(0.1f, crawlidAtlas.findRegions("Death Land"), Animation.PlayMode.NORMAL);

        mossflyShake = new Animation<>(0.4f, mossflyAtlas.findRegions("Shake"), Animation.PlayMode.LOOP);
        mossflyAppear = new Animation<>(0.1f, mossflyAtlas.findRegions("Appear"), Animation.PlayMode.NORMAL);
        mossflyTurnToFly = new Animation<>(0.1f, mossflyAtlas.findRegions("TurnToFly"), Animation.PlayMode.NORMAL);
        mossflyFly = new Animation<>(0.1f, mossflyAtlas.findRegions("Fly"), Animation.PlayMode.LOOP);
        mossflyDeathAir = new Animation<>(0.1f, mossflyAtlas.findRegions("Death Air"), Animation.PlayMode.LOOP);
        mossflyDeathLand = new Animation<>(0.1f, mossflyAtlas.findRegions("Death Land"), Animation.PlayMode.NORMAL);

        huskIdle = new Animation<>(0.15f, huskAtlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        huskWalk = new Animation<>(0.1f, huskAtlas.findRegions("Walk"), Animation.PlayMode.LOOP);
        huskAnticipate = new Animation<>(0.08f, huskAtlas.findRegions("Attack Anticipate"), Animation.PlayMode.NORMAL);
        huskLunge = new Animation<>(0.08f, huskAtlas.findRegions("Attack Lunge"), Animation.PlayMode.LOOP);
        huskCooldown = new Animation<>(0.1f, huskAtlas.findRegions("Attack Cooldown"), Animation.PlayMode.NORMAL);
        huskTurn = new Animation<>(0.1f, huskAtlas.findRegions("Turn"), Animation.PlayMode.NORMAL);
        huskDeathAir = new Animation<>(0.1f, huskAtlas.findRegions("Death Air"), Animation.PlayMode.LOOP);
        huskDeathLand = new Animation<>(0.1f, huskAtlas.findRegions("Death Land"), Animation.PlayMode.NORMAL);

        guardianIdle = new Animation<>(0.15f, guardianAtlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        guardianRun = new Animation<>(0.08f, guardianAtlas.findRegions("Run"), Animation.PlayMode.LOOP);
        guardianShoot = new Animation<>(0.08f, guardianAtlas.findRegions("Shoot"), Animation.PlayMode.NORMAL);
        guardianTurn = new Animation<>(0.08f, guardianAtlas.findRegions("Turn"), Animation.PlayMode.NORMAL);
        guardianDeathAir = new Animation<>(0.1f, guardianAtlas.findRegions("Death Air"), Animation.PlayMode.LOOP);
        guardianDeathLand = new Animation<>(0.1f, guardianAtlas.findRegions("Death Land"), Animation.PlayMode.NORMAL);
        laserBeamRegion = guardianAtlas.findRegion("laserBeam");
        laserOrbAnim = new Animation<>(0.05f, guardianAtlas.findRegions("LaserCircle"), Animation.PlayMode.LOOP);
    }

    public void render(SpriteBatch batch, LevelModel levelModel, float deltaTime) {

        for (EnemyModel enemy : levelModel.getEnemies()) {

            EnemyViewData viewData = viewDataMap.computeIfAbsent(enemy, k -> new EnemyViewData());
            EnemyState currentState = enemy.getCurrentState();


            if (enemy.isDead() && !viewData.wasDead) {

                viewData.stateTime = 0.0f;
                viewData.wasDead = true;
            }
            else if (!enemy.isDead() && currentState != null && currentState.getClass() != viewData.previousStateType) {

                viewData.stateTime = 0.0f;
                viewData.previousStateType = currentState.getClass();
            }

            viewData.stateTime += deltaTime;
            TextureRegion currentFrame = determineFrame(enemy, currentState, viewData.stateTime);

            float hitboxX = (float) enemy.getX();
            float hitboxY = (float) enemy.getY();

            float hitboxHeight = (float) enemy.getHeight();
            float hitboxWidth = (float) enemy.getWidth();

            if (currentFrame != null) {

                boolean shouldFaceRight = enemy.isFacingRight();
                if (currentFrame.isFlipX() != shouldFaceRight) {
                    currentFrame.flip(true, false);
                }

                float regionWidth = currentFrame.getRegionWidth();
                float regionHeight = currentFrame.getRegionHeight();

                float renderX = hitboxX - (regionWidth - hitboxWidth) / 2.0f;
                float renderY = hitboxY;

                batch.draw(
                    currentFrame,
                    renderX,
                    renderY,
                    regionWidth,
                    regionHeight
                );
            }

            if (enemy instanceof CrystalGuardianModel) {
                CrystalGuardianModel guardian = (CrystalGuardianModel) enemy;
                com.badlogic.gdx.math.Rectangle laser = guardian.getActiveLaserBounds();

                if (laser != null) {

                    EnemyViewData vData = viewDataMap.get(enemy);
                    float progress = Math.min((vData.stateTime - 0.6f) / 0.4f, 1.0f);
                    if(progress < 0) progress = 0;

                    float thicknessScale = (float) Math.sin(progress * Math.PI);

                    float beamY = laser.y;
                    batch.draw(
                        laserBeamRegion,
                        laser.x, beamY,
                        0, laserBeamRegion.getRegionHeight() / 2f,
                        laser.width, laserBeamRegion.getRegionHeight(),
                        1.0f, thicknessScale,
                        0
                    );


                    TextureRegion orbFrame = laserOrbAnim.getKeyFrame(vData.stateTime);
                    float orbX = guardian.isFacingRight() ?
                        (float)(guardian.getX() + guardian.getWidth() - 20) :
                        (float)(guardian.getX() - orbFrame.getRegionWidth() + 20);

                    float orbY = laser.y - (orbFrame.getRegionHeight() / 2f) + (laserBeamRegion.getRegionHeight() / 2f);

                    batch.draw(
                        orbFrame,
                        orbX, orbY,
                        orbFrame.getRegionWidth() / 2f, orbFrame.getRegionHeight() / 2f,
                        orbFrame.getRegionWidth(), orbFrame.getRegionHeight(),
                        thicknessScale * 1.5f, thicknessScale * 1.5f,
                        0
                    );
                }
            }
        }

        cleanupViewData(levelModel);
    }

    private void cleanupViewData(LevelModel levelModel) {

        viewDataMap.keySet().retainAll(levelModel.getEnemies());
    }

    private TextureRegion determineFrame(EnemyModel enemy, EnemyState state, float stateTime) {

        if (enemy.isDead()) {

            if (!enemy.isGrounded()) {

                if (enemy instanceof CrawlidModel) return crawlidDeathAir.getKeyFrame(stateTime);
                if (enemy instanceof MossflyModel) return mossflyDeathAir.getKeyFrame(stateTime);
                if (enemy instanceof HuskHornHeadModel) return huskDeathAir.getKeyFrame(stateTime);
                if (enemy instanceof CrystalGuardianModel) return guardianDeathAir.getKeyFrame(stateTime);
            }
            else {

                if (enemy instanceof CrawlidModel) return crawlidDeathLand.getKeyFrame(stateTime);
                if (enemy instanceof MossflyModel) return mossflyDeathLand.getKeyFrame(stateTime);
                if (enemy instanceof HuskHornHeadModel) return huskDeathLand.getKeyFrame(stateTime);
                if (enemy instanceof CrystalGuardianModel) return guardianDeathLand.getKeyFrame(stateTime);
            }
        }

        if (state == null) return null;

        if (state instanceof EnemyTurnState) {

            if (enemy instanceof CrawlidModel) return crawlidTurn.getKeyFrame(stateTime);
            if (enemy instanceof HuskHornHeadModel) return huskTurn.getKeyFrame(stateTime);
            if (enemy instanceof CrystalGuardianModel) return guardianTurn.getKeyFrame(stateTime);
        }

        if (enemy instanceof CrawlidModel) {

            return crawlidWalk.getKeyFrame(stateTime);
        }
        else if (enemy instanceof MossflyModel) {

            if (state instanceof MossflyHiddenState) {

                return mossflyShake.getKeyFrame(stateTime);
            }
            if (state instanceof MossflyEmergeState) {

                if (mossflyAppear.isAnimationFinished(stateTime)) {

                    return mossflyTurnToFly.getKeyFrame(stateTime - mossflyAppear.getAnimationDuration());
                }
                return mossflyAppear.getKeyFrame(stateTime);
            }
            if (state instanceof MossflyHoverState || state instanceof MossflyChargeState) {

                return mossflyFly.getKeyFrame(stateTime);
            }
        }
        else if (enemy instanceof HuskHornHeadModel) {

            if (state instanceof HuskRestState) {

                return huskIdle.getKeyFrame(stateTime);
            }
            if (state instanceof HuskPatrolState) {

                return huskWalk.getKeyFrame(stateTime);
            }
            if (state instanceof HuskChargeState) {

                if (huskAnticipate.isAnimationFinished(stateTime)) {

                    return huskLunge.getKeyFrame(stateTime - huskAnticipate.getAnimationDuration());
                }
                return huskAnticipate.getKeyFrame(stateTime);
            }
            if (state instanceof Main.core.model.enemy.state.huskHornHeadState.HuskCooldownState) {

                return huskCooldown.getKeyFrame(stateTime);
            }
        }
        else if (enemy instanceof CrystalGuardianModel) {

            if (state instanceof GuardianIdleState) {

                return guardianIdle.getKeyFrame(stateTime);
            }
            if (state instanceof GuardianShootState) {

                return guardianShoot.getKeyFrame(stateTime);
            }
            if (state instanceof GuardianEnragedState) {

                return guardianRun.getKeyFrame(stateTime);
            }
        }

        return null;
    }
}
