package Main.core.model.enemy.state.mossflyState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class MossflyChargeState implements EnemyState {

    private final double targetX;
    private final double targetY;
    private final double CHARGE_SPEED = 350.0;
    private final double ARRIVAL_THRESHOLD = 10.0;

    public MossflyChargeState(double targetX, double targetY) {

        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void enter(EnemyModel enemy) {

        double deltaX = targetX - enemy.getX();
        double deltaY = targetY - enemy.getY();

        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

        if (distance > 0) {

            enemy.setXVelocity((deltaX / distance) * CHARGE_SPEED);
            enemy.setYVelocity((deltaY / distance) * CHARGE_SPEED);

            enemy.setFacingRight(deltaX > 0);
        }
    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {

        double currentDeltaX = targetX - enemy.getX();
        double currentDeltaY = targetY - enemy.getY();
        double distanceToTarget = Math.sqrt((currentDeltaX * currentDeltaX) + (currentDeltaY * currentDeltaY));

        if (distanceToTarget <= ARRIVAL_THRESHOLD) {

            return new MossflyHoverState();
        }

        return this;
    }

    @Override
    public void exit(EnemyModel enemy) {

        enemy.setXVelocity(0);
        enemy.setYVelocity(0);
    }
}
