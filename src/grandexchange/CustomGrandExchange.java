package grandexchange;

import Core.CombatTrainer;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;
import utility.Sleep;


public class CustomGrandExchange {

    private final CombatTrainer parent;

    public CustomGrandExchange(CombatTrainer script) {
        this.parent = script;
    }


    public boolean BuyItemAndCollect(String itemName, int amount) throws InterruptedException {
        if (!parent.getGrandExchange().isOpen()) {
            NPC exchangeWorker = parent.getNpcs().closest("Grand Exchange Clerk");
            exchangeWorker.interact("Exchange");

            Sleep.sleepUntil(() -> parent.getGrandExchange().isOpen(), 3000);
        }

        QuickExchange quickExchange = new QuickExchange(parent);
        if (!quickExchange.quickBuy(itemName, amount, true, parent)) {
            parent.getGrandExchange().close();
            Sleep.sleepUntil(() -> !parent.getGrandExchange().isOpen(), 2000);
            return false;
        } else {
            return true;
        }
    }
}