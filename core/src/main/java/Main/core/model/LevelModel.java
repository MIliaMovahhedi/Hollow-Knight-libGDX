package Main.core.model;

import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;
import Main.core.model.zote.ZoteModel;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelModel {

    private List<Rectangle> groundBounds;
    private List<Rectangle> hazardBounds;
    private List<EnemyModel> enemies;
    private List<TransitionDoor> transitionDoors;
    private ZoteModel zote;

    public LevelModel() {

        this.groundBounds = new ArrayList<>();
        this.hazardBounds = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.transitionDoors = new ArrayList<>();
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
    public void addTransitionDoor(TransitionDoor door) { transitionDoors.add(door); }
    public void addZote(ZoteModel zote) {
        this.zote = zote;
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
    public List<TransitionDoor> getTransitionDoors() { return transitionDoors; }

    public void updateEnemies(double deltaTime, KnightModel knight) {

        Iterator<EnemyModel> iterator = enemies.iterator();

        while (iterator.hasNext()) {

            EnemyModel enemy = iterator.next();
            enemy.update(deltaTime, knight, this);

            if (enemy.canBeRemoved()) {
                iterator.remove();
            }
        }

        if (zote != null) {
            zote.update((float) deltaTime, knight);
        }
    }

    public ZoteModel getZote() {

        return zote;
    }
}
