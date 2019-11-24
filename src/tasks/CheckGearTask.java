package tasks;

import Core.CombatTrainer;
import Core.States;
import grandexchange.Bank;
import utility.Sleep;

public class CheckGearTask extends Task {

    public CheckGearTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        return !script.outOfGold && !script.itemsHaveBeenChecked && script.currentLocation == null;
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.CHECK_ITEMS;
        if (script.getWalking().webWalk(Bank.closestTo(script.myPlayer()))) {
            script.getBank().open();
            Sleep.sleepUntil(() -> script.getBank().isOpen(), 5000);
            script.missingItems = script.getMissingItems();
            script.currentTier = script.getCurrentTier();
            script.itemsHaveBeenChecked = true;
        }
    }

}