package data;

import Core.CombatTrainer;
import org.osbot.rs07.api.ui.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class Equipment {

    private final String amulet = "Amulet of strength";
    private String[] itemElements = { "Iron", "Black", "Mithril", "Adamant", "Rune" };
    private String[] itemTypes = { "scimitar", "full helm", "kiteshield", "platebody", "platelegs" };
    private EquipmentSlot[] itemSlots = { EquipmentSlot.WEAPON, EquipmentSlot.HAT, EquipmentSlot.SHIELD, EquipmentSlot.CHEST, EquipmentSlot.LEGS };

    public Equipment() {

    }

    public List<TierEquipment> getTierItems(int tier) {
        List<TierEquipment> items = new ArrayList<>();
        for(int i = 0; i < CombatTrainer.MAX_TIER; i++) {
            String itemName = itemElements[tier - 1] + " " + itemTypes[i];
            if (itemName.equals("Rune platebody"))
                itemName = "Rune chainbody";
            items.add(new TierEquipment(itemName, itemSlots[i]));
        }
        items.add(new TierEquipment(amulet, EquipmentSlot.AMULET));
        return items;
    }
}
