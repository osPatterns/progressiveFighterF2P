package data;

import org.osbot.rs07.api.ui.EquipmentSlot;

public class TierEquipment {

    private String name;
    private EquipmentSlot slot;

    public TierEquipment(String name, EquipmentSlot slot) {
        this.name = name;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public int getLevelRequirement() {
        int index = name.indexOf(' ');
        String itemType = name.substring(0, index);
        switch(itemType) {
            case "Iron":
                return 1;
            case "Black":
                return 10;
            case "Mithril":
                return 20;
            case "Adamant":
                return 30;
            case "Rune":
                return 40;
        }
        return 0;
    }
}
