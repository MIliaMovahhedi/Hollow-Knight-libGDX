package Main.core.model.enemy;

import Main.core.model.LevelModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.mossflyState.MossflyHoverState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

public class MossflyModel extends EnemyModel {

    private final double GRAVITY = -900.0;

    public MossflyModel(double startX, double startY) {

        super(startX, startY, 48, 48, 3.0, 1.0);
        changeState(new MossflyHoverState());
    }

    @Override
    public void update(double deltaTime, KnightModel knight, LevelModel levelModel) {

        updateDeath(deltaTime);

        if (isDead()) {

            applyGravity(GRAVITY, deltaTime);
        }
        else if (currentState != null) {

            EnemyState newState = currentState.update(this, knight, levelModel, deltaTime);
            if (newState != currentState) {

                changeState(newState);
            }
        }

        handlePhysicsBounds(deltaTime, levelModel);
    }

    private void handlePhysicsBounds(double deltaTime, LevelModel levelModel) {

        double nextX = this.x + (xVelocity * deltaTime);
        bounds.setX((float) nextX);

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (bounds.overlaps(rect)) {

                if (xVelocity > 0) {

                    nextX = rect.getX() - bounds.getWidth();
                }
                else if (xVelocity < 0) {

                    nextX = rect.getX() + rect.getWidth();
                }

                xVelocity = 0;
                bounds.setX((float) nextX);
                break;
            }
        }
        this.x = nextX;

        double nextY = this.y + (yVelocity * deltaTime);
        bounds.setY((float) nextY);
        isGrounded = false;

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (bounds.overlaps(rect)) {

                if (yVelocity > 0) {

                    nextY = rect.getY() - bounds.getHeight();
                    yVelocity = 0;
                }
                else if (yVelocity < 0) {

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
