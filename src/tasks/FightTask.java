package tasks;

import Core.CombatTrainer;
import Core.States;
import data.Locations;
import org.osbot.rs07.api.model.NPC;
import utility.Sleep;

import java.util.Arrays;

public class FightTask extends Task {

    public FightTask(CombatTrainer script) {
        super(script);
    }

    @Override
    public boolean canProcess() {
        return script.currentLocation != null && script.getInventory().contains(script.settings.getFood()) && script.myPlayer().getHealthPercent() > 50 && script.itemsHaveBeenChecked;
    }

    @Override
    public void process() throws InterruptedException {
        script.currentState = States.FIGHT;

        if (script.currentLocation == null)
            new CheckGearTask(script).process();

        if (!script.getSettings().isRunning() && script.getSettings().getRunEnergy() > 10)
            script.getSettings().setRunning(true);

        if (script.myPlayer().isUnderAttack() || script.getCombat().isFighting() || script.myPlayer().getInteracting() != null) {
            Sleep.sleepUntil(() -> !script.myPlayer().isUnderAttack(), 60000);
        }

        NPC enemy = script.getNpcs().closest(npc -> Arrays.asList(script.currentLocation.getNpcs()).contains(npc.getName()) && npc.isAttackable());

        if (enemy == null) {
            script.log("The Npc we're trying to attack couldn't be found! Please report this error if it persists.");
            script.getWalking().webWalk(script.currentLocation.getArea());
            return;
        }

        if (script.getMap().canReach(enemy.getPosition())) {
            if (!script.getCombat().isFighting() && !enemy.isUnderAttack() && !enemy.isAnimating() && !script.myPlayer().isMoving()) {
                if (!Locations.isInCurrentLocation(script, enemy)) {
                    script.getWalking().walk(script.currentLocation.getArea());
                } else {
                    enemy.interact("Attack");
                    Sleep.sleepUntil(() -> !script.getCombat().isFighting() || script.myPlayer().getHealthPercent() < 50, 60000);
                }
            }
        } else {
            script.getWalking().webWalk(enemy.getPosition());
        }
    }

}