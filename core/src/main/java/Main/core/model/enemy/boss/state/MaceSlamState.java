package Main.core.model.enemy.boss.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.boss.FalseKnightModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class MaceSlamState implements EnemyState {

    private double timer = 0.0;
    private final double windupDuration = 0.6;
    private final double activeDuration = 0.2;
    private final double recoveryDuration = 0.5;
    private final float slamRange = 150.0f;
    private boolean strikeActive = false;

    @Override
    public void enter(EnemyModel enemy) {

        enemy.setXVelocity(0);
        timer = 0.0;
        strikeActive = false;
        if (enemy instanceof FalseKnightModel) {

            ((FalseKnightModel) enemy).setActiveWeaponBounds(null);
        }
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        timer += deltaTime;
        if (timer < windupDuration) {

            boolean targetIsRight = knightModel.getX() > enemy.getX();
            enemy.setFacingRight(targetIsRight);
        }
        else if (timer < windupDuration + activeDuration) {

            if (!strikeActive) {

                generateSlamHitbox(enemy);
                strikeActive = true;
            }
        }
        else {

            if (strikeActive) {

                if (enemy instanceof FalseKnightModel) {

                    ((FalseKnightModel) enemy).setActiveWeaponBounds(null);
                }
                strikeActive = false;
            }

            if (timer >= windupDuration + activeDuration + recoveryDuration) {

                return null;
            }
        }
        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        if (enemy instanceof FalseKnightModel) {

            ((FalseKnightModel) enemy).setActiveWeaponBounds(null);
        }
    }

    private void generateSlamHitbox(EnemyModel enemy) {

        float dynamicSlamRange = slamRange * 1.3f;
        float slamHeight = (float) enemy.getHeight() * 0.25f;

        float hitX = enemy.isFacingRight() ?
            (float) (enemy.getX() + enemy.getWidth()) :
            (float) (enemy.getX() - dynamicSlamRange);

        Rectangle slamHitbox = new Rectangle(
            hitX,
            (float) enemy.getY(),
            dynamicSlamRange,
            slamHeight
        );

        if (enemy instanceof FalseKnightModel) {
            ((FalseKnightModel) enemy).setActiveWeaponBounds(slamHitbox);
        }
    }
}
