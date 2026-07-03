package Main.core.model;

import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelModel {

    private List<Rectangle> groundBounds;
    private List<Rectangle> hazardBounds;
    private List<EnemyModel> enemies;

    public LevelModel() {

        this.groundBounds = new ArrayList<>();
        this.hazardBounds = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    public void addGroundBound(Rectangle bound) {
        groundBounds.add(bound);
    }
    public void addHazardBound(Rectangle bound) {
        hazardBounds.add(bound);
    }
    public void addEnemy(EnemyModel enemy) {
        enemies.add(enemy);
    }

    public List<Rectangle> getGroundBounds() {
        return groundBounds;
    }
    public List<Rectangle> getHazardBounds() {
        return hazardBounds;
    }
    public List<EnemyModel> getEnemies() {
        return enemies;
    }

    public void updateEnemies(double deltaTime, KnightModel knight) {

        Iterator<EnemyModel> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            EnemyModel enemy = iterator.next();
            enemy.update(deltaTime, knight, this);

            if (enemy.isDead()) {

                enemy.updateDeath(deltaTime);
                if (enemy.canBeRemoved()) {

                    iterator.remove();
                }
            }
            else {

                enemy.update(deltaTime, knight, this);
            }
        }
    }
}
