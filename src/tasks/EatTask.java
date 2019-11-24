package tasks;

import Core.CombatTrainer;
import Core.States;
import org.osbot.rs07.event.Event;
import utility.Sleep;

public class EatTask extends Task {

    public EatTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        return script.myPlayer().getHealthPercent() < 50;
    }

    @Override
    public void process() throws InterruptedException {

        if (script.myPlayer().getHealthPercent() < 50) {
            script.getInventory().interact("Eat", script.settings.getFood());
            Sleep.sleepUntil(() -> script.myPlayer().getHealthPercent() > 50, 1000);
        }

        if (!script.getInventory().contains(script.settings.getFood())) {
            script.currentLocation = null;
            new WithdrawFoodTask(script).process();
        }
    }
}