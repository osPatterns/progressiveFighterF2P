package tasks;

import Core.CombatTrainer;
import Core.States;
import grandexchange.CustomGrandExchange;
import org.osbot.rs07.api.map.constants.Banks;
import utility.Sleep;

public class PurchaseFoodTask extends Task {

    public PurchaseFoodTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        if (!script.getBank().isOpen())
            return false;
        return !script.getBank().contains(script.settings.getFood());
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.PURCHASE_FOOD;
        if (script.getWalking().webWalk(Banks.GRAND_EXCHANGE)) {
            String itemName = script.settings.getFood();
            int amount = 500;

            if (!script.getInventory().contains(995)) {
                if (!script.getBank().isOpen()) {
                    script.getBank().open();
                    Sleep.sleepUntil(() -> script.getBank().isOpen(), 1500);
                }

                if (!script.getInventory().isEmpty())
                    script.getBank().depositAll();

                script.getBank().withdrawAll(995);
                script.getBank().close();
            }

            CustomGrandExchange customGE = new CustomGrandExchange(script);
            customGE.BuyItemAndCollect(itemName, amount);

            if (!script.getBank().isOpen()) {
                script.getBank().open();
                Sleep.sleepUntil(() -> script.getBank().isOpen(), 1000);
                script.getBank().depositAll();
            }
        }
    }

}