package Main.core.model.enemy.state.huskHornHeadState;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.knight.KnightModel;

public class HuskChargeState implements EnemyState {

    @Override
    public void enter(EnemyModel enemy) {

    }

    @Override
    public EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime) {
        return null;
    }

    @Override
    public void exit(EnemyModel enemy) {

    }
}
