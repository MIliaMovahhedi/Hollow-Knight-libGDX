package Main.core.model.charm;

import java.util.List;

public interface CharmObserver {

    void onCharmsUpdated(List<Charm> charms);
}
