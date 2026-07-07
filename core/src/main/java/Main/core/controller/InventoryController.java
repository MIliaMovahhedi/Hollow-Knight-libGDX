package Main.core.controller;

import Main.core.model.charm.Charm;
import Main.core.model.charm.CharmManager;
import Main.core.model.enums.ScreenState;
import Main.core.model.knight.KnightModel;
import Main.core.view.ScreenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryController {

    private final int maxNotches = 3;
    private int usedNotches = 0;
    private final List<String> equippedCharms;
    private final Map<String, Integer> charmCosts;
    private final Map<String, String> charmDescriptions;
    private final Map<String, String> charmDisplayNames;
    private KnightModel knightModel;

    public InventoryController() {
        equippedCharms = new ArrayList<>();
        charmCosts = new HashMap<>();
        charmDescriptions = new HashMap<>();
        charmDisplayNames = new HashMap<>();

        String dashmaster = "Dashmaster - _0011_charm_generic_03";
        charmDisplayNames.put(dashmaster, "Dashmaster");
        charmCosts.put(dashmaster, 1);
        charmDescriptions.put(dashmaster, "The bearer will be able to dash more often as well as dash downwards. Perfect for those who want to move around quickly.");

        String quickFocus = "Quick Focus - _0005_charm_fast_focus";
        charmDisplayNames.put(quickFocus, "Quick Focus");
        charmCosts.put(quickFocus, 1);
        charmDescriptions.put(quickFocus, "Increases the speed of focusing SOUL, allowing the bearer to heal damage faster.");

        String quickSlash = "Quick Slash - _0003_charm_nail_slash_speed_up";
        charmDisplayNames.put(quickSlash, "Quick Slash");
        charmCosts.put(quickSlash, 1);
        charmDescriptions.put(quickSlash, "Born from imperfect, discarded nails that have fused together. Allows the bearer to slash much more rapidly with their nail.");

        String soulCatcher = "Soul Catcher - _0001_charm_more_soul";
        charmDisplayNames.put(soulCatcher, "Soul Catcher");
        charmCosts.put(soulCatcher, 1);
        charmDescriptions.put(soulCatcher, "Used by shamans to draw more SOUL from the world around them. Increases the amount of SOUL gained when striking an enemy with the nail.");

        String strength = "Unbreakable Strength_0002_charm_glass_attack_up_full";
        charmDisplayNames.put(strength, "Unbreakable Strength");
        charmCosts.put(strength, 1);
        charmDescriptions.put(strength, "Strengthens the bearer, increasing the damage they deal to enemies with their nail. This charm is unbreakable.");
    }

    public void setKnightModel(KnightModel knightModel) {
        this.knightModel = knightModel;
        syncWithModel();
    }

    public void syncWithModel() {
        equippedCharms.clear();
        usedNotches = 0;

        if (knightModel == null) {
            return;
        }

        CharmManager manager = knightModel.getCharmManager();
        if (manager != null) {
            for (Charm charm : manager.getEquippedCharms()) {
                equippedCharms.add(getCharmId(charm));
                usedNotches += charm.getNotchCost();
            }
        }
    }

    private Charm mapToCharm(String charmId) {
        if (charmId.equals("Dashmaster - _0011_charm_generic_03")) return Charm.DASHMASTER;
        if (charmId.equals("Quick Focus - _0005_charm_fast_focus")) return Charm.QUICK_FOCUS;
        if (charmId.equals("Quick Slash - _0003_charm_nail_slash_speed_up")) return Charm.QUICK_SLASH;
        if (charmId.equals("Soul Catcher - _0001_charm_more_soul")) return Charm.SOUL_CATCHER;
        if (charmId.equals("Unbreakable Strength_0002_charm_glass_attack_up_full")) return Charm.UNBREAKABLE_STRENGTH;
        return null;
    }

    private String getCharmId(Charm charm) {
        return switch (charm) {
            case DASHMASTER -> "Dashmaster - _0011_charm_generic_03";
            case QUICK_FOCUS -> "Quick Focus - _0005_charm_fast_focus";
            case QUICK_SLASH -> "Quick Slash - _0003_charm_nail_slash_speed_up";
            case SOUL_CATCHER -> "Soul Catcher - _0001_charm_more_soul";
            case UNBREAKABLE_STRENGTH -> "Unbreakable Strength_0002_charm_glass_attack_up_full";
        };
    }

    public void goBackToMainMenu() {
        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }

    public boolean toggleCharm(String charmId) {
        if (knightModel == null) {
            return false;
        }

        Charm charm = mapToCharm(charmId);
        if (charm == null) {
            return false;
        }

        CharmManager manager = knightModel.getCharmManager();

        if (manager.getEquippedCharms().contains(charm)) {
            manager.unequipCharm(charm);
            syncWithModel();
            return true;
        } else {
            if (manager.equipCharm(charm)) {
                syncWithModel();
                return true;
            }
        }
        return false;
    }

    public int getUsedNotches() {
        return usedNotches;
    }

    public int getMaxNotches() {
        return maxNotches;
    }

    public List<String> getEquippedCharms() {
        return equippedCharms;
    }

    public String getDescription(String charmId) {
        return charmDescriptions.getOrDefault(charmId, "Unknown Charm.");
    }

    public String getDisplayName(String charmId) {
        return charmDisplayNames.getOrDefault(charmId, "Unknown");
    }
}
