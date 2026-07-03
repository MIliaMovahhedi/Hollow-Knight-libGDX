package Main.core.model.enemy;

import Main.core.model.LevelModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.crystalGuardianState.GuardianIdleState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class CrystalGuardianModel extends EnemyModel {

    private final double GRAVITY = -900.0;
    private Rectangle activeLaserBounds;

    public CrystalGuardianModel(double startX, double startY) {

        super(startX, startY, 90, 110, 20.0, 2.0);
        changeState(new GuardianIdleState());
    }

    @Override
    public void update(double deltaTime, KnightModel knightModel, LevelModel levelModel) {

        if (currentState != null) {
            EnemyState newState = currentState.update(this, knightModel, levelModel, deltaTime);
            if (newState != currentState) {
                changeState(newState);
            }
        }

        applyGravity(GRAVITY, deltaTime);
        handlePhysicsBounds(deltaTime, levelModel);
    }

    public void setActiveLaserBounds(Rectangle bounds) {

        this.activeLaserBounds = bounds;
    }

    public Rectangle getActiveLaserBounds() {

        return activeLaserBounds;
    }

    private void handlePhysicsBounds(double deltaTime, LevelModel levelModel) {

        double nextY = this.y + (yVelocity * deltaTime);
        bounds.setY((float) nextY);
        isGrounded = false;

        for (Rectangle rect : levelModel.getGroundBounds()) {
            if (bounds.overlaps(rect)) {
                if (yVelocity > 0) {
                    nextY = rect.getY() - bounds.getHeight();
                    yVelocity = 0;
                } else if (yVelocity < 0) {
                    nextY = rect.getY() + rect.getHeight();
                    yVelocity = 0;
                    isGrounded = true;
                }
                bounds.setY((float) nextY);
                break;
            }
        }
        this.y = nextY;
    }
}
