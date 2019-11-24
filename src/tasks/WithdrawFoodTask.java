package tasks;

import Core.CombatTrainer;
import Core.States;
import grandexchange.Bank;
import utility.Sleep;

public class WithdrawFoodTask extends Task {

    public WithdrawFoodTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        return !script.getInventory().contains(script.settings.getFood());
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.WITHDRAW_FOOD;
        script.currentLocation = null;

        if (script.getWalking().webWalk(Bank.closestTo(script.myPlayer()))) {
            if (!script.getBank().isOpen()) {
                script.getBank().open();
                Sleep.sleepUntil(() -> script.getBank().isOpen(), 2000);
            }

            if (script.outOfGold && !script.getBank().contains(script.settings.getFood())) {
                script.log("Out of money, out of food. Stopping script.");
                script.stop();
            }

            if (!script.getInventory().isEmpty()) {
                script.getBank().depositAll();
                Sleep.sleepUntil(() -> script.getInventory().isEmpty(), 1000);
            }

            if (script.getBank().contains(script.settings.getFood())) {
                script.getBank().withdrawAll(script.settings.getFood());
                Sleep.sleepUntil(() -> script.getInventory().contains(script.settings.getFood()), 3000);
            } else {
                script.log("Should be buying food here...");
            }

            script.getBank().close();
        }
    }

}