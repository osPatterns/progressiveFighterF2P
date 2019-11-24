package Core;

import tasks.*;
import data.*;
import org.osbot.rs07.api.ui.Tab;
import utility.Sleep;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import visuals.ScriptGUI;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

enum TrainableSkills {
    Attack,
    Strength,
    Defence
}

@ScriptManifest(author = "Patterns", name = "F2P Combat Trainer", info = "Progressive F2P Combat Leveling", version = 0.1, logo = "")
public class CombatTrainer extends Script  {

    private ScriptGUI gui;

    public States currentState;
    public Settings settings;

    public Location currentLocation;
    public Location proposedLocation;

    private TrainableSkills previouslyTrainedSkill;

    public int currentTier;
    public boolean tierIsLocked = false;
    public boolean outOfGold = false;
    public boolean itemsHaveBeenChecked = false;

    public List<String> missingItems;
    public static final int MAX_TIER = 5;

    public ArrayList<Task> tasks = new ArrayList<>();

    @Override
    public void onStart() throws InterruptedException {
        initializeGUI();

        getBot().addPainter(new Paint(this));

        getKeyboard().typeString("::toggleroofs");

        currentTier = getCurrentTier();
        setTierHandicap();

        currentState = States.STARTING_UP;

        missingItems = getMissingItems();
        if (missingItems.size() == 0) {
            itemsHaveBeenChecked = true;
            currentLocation = Locations.getCurrentLocation(this);
        }

        tasks.add(new CheckGearTask(this));
        tasks.add(new BrassKeyTask(this));
        tasks.add(new EquipGearTask(this));
        tasks.add(new FightTask(this));
        tasks.add(new PurchaseFoodTask(this));
        tasks.add(new PurchaseGearTask(this));
        tasks.add(new WalkToCombatTask(this));
        tasks.add(new WithdrawFoodTask(this));
        tasks.add(new EatTask(this));
    }

    public int getCurrentTier() {
        if (tierIsLocked)
            return currentTier;

        int tier = 1;
        int threshold = 10;
        while(true && tier != MAX_TIER) {
            if (meleeSkillsAreOverThreshold(threshold)) {
                tier++;
                threshold += 10;
                continue;
            }
            break;
        }

        return tier;
    }

    public boolean canPurchaseNewGear() {
        if (outOfGold || tierItemsAreEquipped() || !settings.upgradeGear()) {
            return false;
        }
        return true;
    }

    public boolean tierItemsAreEquipped() {
        Equipment items = new Equipment();
        List<TierEquipment> items1 = items.getTierItems(currentTier);
        //return new Equipment().getTierItems(currentTier).stream().filter(item -> !getEquipment().isWearingItem(item.getSlot(), item.getName()))
          //      .collect(Collectors.toList()).size() == 0;
        for(int i = 0; i < items1.size(); i++) {
            if (!getEquipment().isWearingItem(items1.get(i).getSlot(), items1.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public List<String> getMissingItems() throws InterruptedException {
        List<String> missingItems = new ArrayList<>();
        if (!settings.upgradeGear())
            return missingItems;

        Equipment items = new Equipment();
        List<TierEquipment> items1 = items.getTierItems(currentTier);

        if (!getBank().isOpen()) {
            getBank().open();
            Sleep.sleepUntil(() -> getBank().isOpen(), 2000);
        }

        for(int i = 0; i < items1.size(); i++) {
            TierEquipment item = items1.get(i);
            if (item.getName().contains("scimitar") && getSkills().getStatic(Skill.ATTACK) < item.getLevelRequirement())
                continue;
            if (!item.getName().contains("scimitar") && getSkills().getStatic(Skill.DEFENCE) < item.getLevelRequirement())
                continue;

            if (!itemIsOwned(item.getName(), item.getSlot())) {
                missingItems.add(item.getName());
                log("Added " + item.getName() + " to list of missing items.");
            }
        }

        sleep(500);
        return missingItems;
    }

    public boolean itemIsOwned(String name, EquipmentSlot slot) {
        if (getInventory().contains(name) || getBank().contains(name) || getEquipment().isWearingItem(slot, name))  {
            return true;
        }
        return false;
    }

    public boolean meleeSkillsAreOverThreshold(int threshold) {
        List<Integer> meleeSkills = new ArrayList<>();
        if (settings.isTrainingAttack())
            meleeSkills.add(getSkills().getStatic(Skill.ATTACK));
        if (settings.isTrainingStrength())
            meleeSkills.add(getSkills().getStatic(Skill.STRENGTH));
        if (settings.isTrainingDefence())
            meleeSkills.add(getSkills().getStatic(Skill.DEFENCE));
        for(int i : meleeSkills) {
            if (i < threshold) {
                return false;
            }
        }
        return true;
    }

    public void setAttackStyles() throws InterruptedException {
        if (previouslyTrainedSkill == getLowestTrainableCombatSkill()) return;

        getTabs().open(Tab.ATTACK);
        Sleep.sleepUntil(() -> getTabs().isOpen(Tab.ATTACK), 1000);

        switch(getLowestTrainableCombatSkill()) {
            case Attack:
                getWidgets().interact(593, 6, "Chop");
                break;
            case Strength:
                getWidgets().interact(593, 10, "Slash");
                break;
            case Defence:
                getWidgets().interact(593, 18, "Block");
                break;
            default:
                log("Error: Wasn't able to find the current lowest combat skill!");
                break;
        }

        sleep(100);
        if (!getCombat().isAutoRetaliateOn()) {
            getCombat().toggleAutoRetaliate(true);
        }
    }

    public TrainableSkills getLowestTrainableCombatSkill() {
        TrainableSkills lowestSkill = null;
        Map<TrainableSkills, Integer> trainableCombatSkills = new HashMap<>();
        if (settings.isTrainingAttack())
            trainableCombatSkills.put(TrainableSkills.Attack, getSkills().getStatic(Skill.ATTACK));
        if (settings.isTrainingStrength())
            trainableCombatSkills.put(TrainableSkills.Strength, getSkills().getStatic(Skill.STRENGTH));
        if (settings.isTrainingDefence())
            trainableCombatSkills.put(TrainableSkills.Defence, getSkills().getStatic(Skill.DEFENCE));

        Map.Entry<TrainableSkills, Integer> min = null;

        for (Map.Entry<TrainableSkills, Integer> entry : trainableCombatSkills.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
                lowestSkill = entry.getKey();
            }
        }
        previouslyTrainedSkill = lowestSkill;
        return lowestSkill;
    }

    public List<String> getOwnedTierItemsNotEquipped() {
        List<TierEquipment> items = new Equipment().getTierItems(currentTier);
        List<String> items2 = new ArrayList<>();

        for(TierEquipment i : items) {
            if (itemIsOwned(i.getName(), i.getSlot()) && !getEquipment().isWearingItem(i.getSlot(), i.getName())) {
                log(i.getName() + " is owned but not equipped.");
                items2.add(i.getName());
            } else {
                log(i.getName() + " is neither owned nor equipped.");
            }
        }

        return items2;
    }

    public void setTierHandicap() {
        if ((getSkills().getStatic(Skill.DEFENCE) < 10 || getSkills().getStatic(Skill.ATTACK) < 10) && currentTier > 2) {
            log("Too low attack or defense level for this tier. Using tier 2 data.");
            currentTier = 2;
            tierIsLocked = true;
        }
    }

    @Override
    public final void onMessage(final Message message) throws InterruptedException {
        if (message.getMessage().contains("just advanced") && !message.getMessage().contains("hitpoints")) {
            if (getCurrentTier() > currentTier) {
                currentTier = getCurrentTier();
                missingItems = getMissingItems();
                itemsHaveBeenChecked = false;
                currentLocation = null;
                proposedLocation = null;
            }
            setAttackStyles();
        }
    }

    @Override
    public int onLoop() {
        tasks.forEach(tasks -> {
            try {
                tasks.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return 500;
    }

    @Override
    public void onExit() {
        if (gui != null) {
            gui.close();
        }
    }

    private void initializeGUI() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new ScriptGUI(this);
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            stop(false);
            return;
        }

        if (!gui.isReady()) {
            stop(false);
            return;
        }
    }
}