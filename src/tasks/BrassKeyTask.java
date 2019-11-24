package tasks;

import Core.CombatTrainer;
import Core.States;
import data.Location;
import data.Locations;
import grandexchange.Bank;
import org.osbot.rs07.event.Event;
import utility.Sleep;

public class BrassKeyTask extends Task {

    public BrassKeyTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        return script.proposedLocation != null && script.proposedLocation.getName().equals(Locations.HILL_GIANTS.getName()) && !script.getInventory().contains("Brass key");
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.BRASS_KEY;
        script.log("Give me brass key");

        if (Bank.insideABank(script)) {
            if (!script.getBank().isOpen()) {
                script.getBank().open();
                Sleep.sleepUntil(() -> script.getBank().isOpen(), 1000);
            }

            if (script.getBank().contains("Brass key")) {
                script.log("Brass key in bank. Withdrawing.");
                if (script.getInventory().isFull()) {
                    script.getBank().deposit(script.settings.getFood(), 1);
                    Sleep.sleepUntil(() -> !script.getInventory().isFull(), 5000);
                }
                script.getBank().withdraw("Brass key", 1);
                script.sleep(50);
            } else {
                script.log("Brass key not in bank or we're not near one. Walking to hill giants and picking the key up on the way.");
                pickUpBrassKey();
            }
        } else {
            pickUpBrassKey();
        }
    }

    private void pickUpBrassKey() throws InterruptedException {
        if (script.getWalking().webWalk(script.proposedLocation.getArea())) {
            if (script.getInventory().isFull()) {
                script.getInventory().interact("Eat", script.settings.getFood());
                Sleep.sleepUntil(() -> !script.getInventory().isFull(), 10000);
            }
            script.getGroundItems().closest("Brass key").interact("Take");
            Sleep.sleepUntil(() -> script.getInventory().contains("Brass key"), 5000);
        }
    }
}