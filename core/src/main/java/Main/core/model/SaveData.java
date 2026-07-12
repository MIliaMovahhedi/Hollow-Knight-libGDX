package Main.core.model;

import Main.core.model.achievement.Achievement;
import Main.core.model.charm.Charm;

import java.util.ArrayList;
import java.util.List;

public class SaveData {

    private int mapIndex;
    private double knightX;
    private double knightY;
    private double currentHealth;
    private int currentSoul;
    private List<Charm> equippedCharms;
    private GameStats gameStats;
    private List<Achievement> unlockedAchievements;

    public SaveData() {
        this.equippedCharms = new ArrayList<>();
        this.unlockedAchievements = new ArrayList<>();
        this.gameStats = new GameStats();
    }

    public int getMapIndex() { return mapIndex; }
    public void setMapIndex(int mapIndex) { this.mapIndex = mapIndex; }

    public double getKnightX() { return knightX; }
    public void setKnightX(double knightX) { this.knightX = knightX; }

    public double getKnightY() { return knightY; }
    public void setKnightY(double knightY) { this.knightY = knightY; }

    public double getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }

    public int getCurrentSoul() { return currentSoul; }
    public void setCurrentSoul(int currentSoul) { this.currentSoul = currentSoul; }

    public List<Charm> getEquippedCharms() { return equippedCharms; }
    public void setEquippedCharms(List<Charm> equippedCharms) { this.equippedCharms = equippedCharms; }

    public GameStats getGameStats() { return gameStats; }
    public void setGameStats(GameStats gameStats) { this.gameStats = gameStats; }

    public List<Achievement> getUnlockedAchievements() { return unlockedAchievements; }
    public void setUnlockedAchievements(List<Achievement> unlockedAchievements) { this.unlockedAchievements = unlockedAchievements; }
}
