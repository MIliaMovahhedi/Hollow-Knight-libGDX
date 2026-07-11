package Main.core.controller;

import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.KnightModel;
import Main.core.view.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class CheatController {

    public void update(KnightModel knight, LevelModel levelModel, GameScreen screen) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                knight.setGodMode(!knight.isGodMode());
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                knight.setNoclip(!knight.isNoclip());
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                knight.setCurrentHealth(knight.getMaxHealth());
                knight.setCurrentSoul(99);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                for (EnemyModel enemy : levelModel.getEnemies()) {
                    if (!enemy.isDead()) {

                        enemy.takeDamage(9999);
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                knight.setHasEmergencyHeal(!knight.hasEmergencyHeal());
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                screen.loadLevel(2);
            }
        }
    }
}
