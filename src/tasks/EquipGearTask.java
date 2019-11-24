package tasks;

import Core.CombatTrainer;
import Core.States;
import org.osbot.rs07.api.ui.EquipmentSlot;
import utility.Sleep;

public class EquipGearTask extends Task {

    public EquipGearTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() throws InterruptedException {
        if (script.getBank().isOpen()) {
            if (script.getOwnedTierItemsNotEquipped().size() > 0)
                return true;
        }
        return false;
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.EQUIP_GEAR;

        if (!script.getBank().isOpen()) {
            script.getBank().open();
            Sleep.sleepUntil(() -> script.getBank().isOpen(), 2000);
        }

        if (!script.getInventory().isEmpty()) {
            script.getBank().depositAll();
            Sleep.sleepUntil(() -> script.getInventory().isEmpty(), 2000);
        }

        for (String i : script.getOwnedTierItemsNotEquipped()) {
            script.log("Withdraw: " + i);
            script.getBank().withdraw(i, 1);
        }

        script.getBank().close();
        Sleep.sleepUntil(() -> script.getBank().close(), 1500);

        for (String i : script.getOwnedTierItemsNotEquipped()) {
            script.getInventory().equipment.equip(EquipmentSlot.CHEST, i);
        }

        script.getBank().open();
    }
}