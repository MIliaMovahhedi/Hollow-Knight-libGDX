package Main.core.model.charm;

public enum Charm {

    SOUL_CATCHER("Soul Catcher", 1),
    DASHMASTER("Dashmaster", 1),
    UNBREAKABLE_STRENGTH("Unbreakable Strength", 1),
    QUICK_SLASH("Quick Slash", 1),
    QUICK_FOCUS("Quick Focus", 1);

    private final String name;
    private final int notchCost;

    Charm (String name, int notchCost) {
        this.name = name;
        this.notchCost = notchCost;
    }

    public int getNotchCost() {
        return notchCost;
    }

    public String getName() {
        return name;
    }
}
