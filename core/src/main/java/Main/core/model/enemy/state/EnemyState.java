package Main.core.model.enemy.state;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;

public interface EnemyState {

    void enter(EnemyModel enemy);
    EnemyState update(EnemyModel enemy, KnightModel knightModel, LevelModel levelModel, double deltaTime);
    void exit(EnemyModel enemy);
}
