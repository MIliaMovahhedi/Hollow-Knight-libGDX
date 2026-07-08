package Main.core.model;

import java.util.HashSet;
import java.util.Set;

public class GameStats {

    private float playTime;
    private int playerDeaths;
    private int totalKills;
    private Set<String> defeatedEnemyTypes;
    private boolean falseKnightDefeated;
    private boolean gameCompleted;

    public GameStats() {
        this.playTime = 0f;
        this.playerDeaths = 0;
        this.totalKills = 0;
        this.defeatedEnemyTypes = new HashSet<>();
        this.falseKnightDefeated = false;
        this.gameCompleted = false;
    }

    public void addPlayTime(float delta) {
        this.playTime += delta;
    }

    public void incrementPlayerDeaths() {
        this.playerDeaths++;
    }

    public void incrementTotalKills() {
        this.totalKills++;
    }

    public void addDefeatedEnemyType(String enemyType) {
        this.defeatedEnemyTypes.add(enemyType);
    }

    public void setFalseKnightDefeated(boolean defeated) {
        this.falseKnightDefeated = defeated;
    }

    public void setGameCompleted(boolean completed) {
        this.gameCompleted = completed;
    }

    public float getPlayTime() {
        return playTime;
    }

    public int getPlayerDeaths() {
        return playerDeaths;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public Set<String> getDefeatedEnemyTypes() {
        return defeatedEnemyTypes;
    }

    public boolean isFalseKnightDefeated() {
        return falseKnightDefeated;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }
}
