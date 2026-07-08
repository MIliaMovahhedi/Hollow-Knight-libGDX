package Main.core.model.achievement;

import Main.core.model.GameStats;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class AchievementManager {

    private static AchievementManager instance;
    private Set<Achievement> unlockedAchievements;
    private List<AchievementObserver> observers;
    private final float speedrunTimeLimit = 3600f;
    private final int totalEnemyTypesCount = 5;

    private AchievementManager() {

        this.unlockedAchievements = EnumSet.noneOf(Achievement.class);
        this.observers = new ArrayList<>();
    }

    public static AchievementManager getInstance() {

        if (instance == null) {

            instance = new AchievementManager();
        }
        return instance;
    }

    public void addObserver(AchievementObserver observer) {

        if (!observers.contains(observer)) {

            observers.add(observer);
        }
    }

    public void removeObserver(AchievementObserver observer) {

        observers.remove(observer);
    }

    public void evaluateAchievements(GameStats stats) {

        if (stats.isGameCompleted() && !unlockedAchievements.contains(Achievement.COMPLETION)) {

            unlock(Achievement.COMPLETION);
        }

        if (stats.isGameCompleted() && stats.getPlayTime() <= speedrunTimeLimit && !unlockedAchievements.contains(Achievement.SPEEDRUN)) {

            unlock(Achievement.SPEEDRUN);
        }

        if (stats.getDefeatedEnemyTypes().size() >= totalEnemyTypesCount && !unlockedAchievements.contains(Achievement.TRUE_HUNTER)) {

            unlock(Achievement.TRUE_HUNTER);
        }

        if (stats.isFalseKnightDefeated() && !unlockedAchievements.contains(Achievement.DEFEAT_FALSE_KNIGHT)) {

            unlock(Achievement.DEFEAT_FALSE_KNIGHT);
        }

        if (stats.isGameCompleted() && stats.getPlayerDeaths() == 0 && !unlockedAchievements.contains(Achievement.UNTOUCHABLE)) {

            unlock(Achievement.UNTOUCHABLE);
        }
    }

    private void unlock(Achievement achievement) {

        unlockedAchievements.add(achievement);
        notifyObservers(achievement);
    }

    private void notifyObservers(Achievement achievement) {

        for (AchievementObserver observer : observers) {

            observer.onAchievementUnlocked(achievement);
        }
    }

    public boolean isUnlocked(Achievement achievement) {

        return unlockedAchievements.contains(achievement);
    }

    public Set<Achievement> getUnlockedAchievements() {

        return unlockedAchievements;
    }
}
