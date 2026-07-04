package Main.core.model.charm;

import java.util.ArrayList;
import java.util.List;

public class CharmManager implements CharmSubject {

    private final int maxNotches = 3;
    private int currentNotchesUsed = 0;

    private List<Charm> equippedCharms;
    private List<CharmObserver> observers;

    public CharmManager() {

        equippedCharms = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public boolean equipCharm(Charm charm) {

        if (equippedCharms.contains(charm)) {

            return false;
        }

        if (currentNotchesUsed + charm.getNotchCost() <= maxNotches) {

            equippedCharms.add(charm);
            currentNotchesUsed += charm.getNotchCost();
            notifyObservers();
            return true;
        }
        return false;
    }

    public void unequipCharm(Charm charm) {

        if (equippedCharms.remove(charm)) {

            currentNotchesUsed -= charm.getNotchCost();
            notifyObservers();
        }
    }

    public List<Charm> getEquippedCharms() {

        return equippedCharms;
    }

    @Override
    public void addObserver(CharmObserver observer) {

        observers.add(observer);
        observer.onCharmsUpdated(equippedCharms);
    }

    @Override
    public void removeObserver(CharmObserver observer) {

        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {

        for (CharmObserver observer : observers) {
            observer.onCharmsUpdated(equippedCharms);
        }
    }
}
