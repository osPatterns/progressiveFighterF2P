package tasks;

import Core.CombatTrainer;
import Core.States;
import grandexchange.CustomGrandExchange;
import org.osbot.rs07.api.map.constants.Banks;
import utility.Sleep;

public class PurchaseGearTask extends Task {

    public PurchaseGearTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        if (!script.getBank().isOpen())
            return false;
        if(script.missingItems == null || script.missingItems.size() == 0)
            return false;
        return script.canPurchaseNewGear();
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.PURCHASE_ITEMS;

        if (script.getWalking().webWalk(Banks.GRAND_EXCHANGE)) {
            openBankWithdrawCoins();

            CustomGrandExchange customGE = new CustomGrandExchange(script);

            if (script.getInventory().isFull() || !script.getInventory().contains(995)) {
                openBankWithdrawCoins();
            }

            for (int i = 0; i < script.missingItems.size(); i++) {
                if (script.outOfGold)
                    break;
                if (!customGE.BuyItemAndCollect(script.missingItems.get(i), 1)) {
                    script.log("FAILED to buy new item.");
                }
            }

            new EquipGearTask(script).process();
        }
    }

    private void openBankWithdrawCoins() throws InterruptedException {
        if (!script.getBank().isOpen()) {
            script.getBank().open();
            Sleep.sleepUntil(() -> script.getBank().isOpen(), 3000);
        }

        if (!script.getInventory().isEmpty()) {
            script.getBank().depositAll();
            Sleep.sleepUntil(() -> script.getInventory().isEmpty(), 1500);
        }

        script.getBank().withdrawAll(995);
        script.getBank().close();
        Sleep.sleepUntil(() -> !script.getBank().isOpen(), 1500);
    }
}