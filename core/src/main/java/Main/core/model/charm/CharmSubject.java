package Main.core.model.charm;

public interface CharmSubject {

    void addObserver(CharmObserver observer);
    void removeObserver(CharmObserver observer);
    void notifyObservers();
}
