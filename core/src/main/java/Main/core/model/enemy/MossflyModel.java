package Main.core.model.enemy;

import Main.core.model.LevelModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.mossflyState.MossflyHoverState;
import Main.core.model.knight.KnightModel;

public class MossflyModel extends EnemyModel {

    public MossflyModel(double startX, double startY) {

        super(startX, startY, 48, 48, 3.0, 1.0);
        changeState(new MossflyHoverState());
    }

    @Override
    public void update(double deltaTime, KnightModel knight, LevelModel levelModel) {

        if (currentState != null) {

            EnemyState newState = currentState.update(this, knight, levelModel, deltaTime);
            if (newState != currentState) {

                changeState(newState);
            }
        }

        this.x += this.xVelocity * deltaTime;
        this.y += this.yVelocity * deltaTime;

        bounds.setX((float) this.x);
        bounds.setY((float) this.y);
    }
}
