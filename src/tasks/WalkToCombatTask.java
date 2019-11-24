package tasks;

import Core.CombatTrainer;
import Core.States;
import data.Location;
import data.Locations;
import org.osbot.rs07.api.ui.Skill;

import java.util.Random;

public class WalkToCombatTask extends Task {

    public WalkToCombatTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        if (script.proposedLocation != null && script.proposedLocation.getName().equals(Locations.HILL_GIANTS.getName()) && !script.getInventory().contains("Brass key"))
            return false;

        return script.getInventory().contains(script.settings.getFood()) && script.currentLocation == null;
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.WALK_TO_COMBAT;

        if (script.proposedLocation == null) {
            script.setTierHandicap();
            int areaLength = Locations.TIER_AREAS[script.currentTier - 1].length;

            Location location = Locations.TIER_AREAS[script.currentTier - 1][new Random().nextInt(areaLength)];
            script.proposedLocation = location;
            script.log("Combat area chosen: " + location.getName());

            if (location.getName().equals(Locations.HILL_GIANTS.getName()) && !script.getInventory().contains("Brass key")) {
                return;
            } else if (script.getWalking().webWalk(location.getArea())) {
                script.setAttackStyles();
                script.currentLocation = location;
            }
        } else {
            if (script.getWalking().webWalk(script.proposedLocation.getArea())) {
                script.currentLocation = script.proposedLocation;
                script.proposedLocation = null;
                script.setAttackStyles();
            }
        }
    }

}