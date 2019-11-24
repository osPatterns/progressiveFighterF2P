package data;

import Core.CombatTrainer;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

import javax.swing.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Locations {

    public static final Position BRASS_KEY_SPAWN = new Position(3131, 9682, 0);

    /*
    Levels 1-10
     */

    public static final Location LUMBRIDGE_WEST_CHICKENS = new Location("Lumbridge west chickens", new Area(new Position(3169, 3288, 0), new Position(3185, 3301, 0)), new String[]{"Chicken"});
    public static final Location LUMBRIDGE_EAST_CHICKENS = new Location("Lumbridge east chickens", new Area(new Position(3225, 3288, 0), new Position(3240, 3304, 0)), new String[]{"Chicken"});

    public static final Location[] TIER_ONE_TRAINING_AREAS = { LUMBRIDGE_WEST_CHICKENS, LUMBRIDGE_EAST_CHICKENS };

    /*
    Levels 10-20
     */

    public static final Location LUMBRIDGE_EAST_GOBLINS = new Location("Lumbridge east goblins", new Area(new Position(3253, 3255, 0), new Position(3259, 3249, 0)), new String[]{"Goblin"});
    public static final Location LUMBRIDGE_WEST_COWS = new Location("Lumbridge west cows", new Area(new Position(3192, 3281, 0), new Position(3209, 3301, 0)), new String[]{"Cow", "Cow calf"});
    public static final Location LUMBRIDGE_EAST_COWS = new Location("Lumbridge east cows", new Area(new Position(3253, 3255, 0), new Position(3211, 3302, 0)), new String[]{"Cow", "Cow calf"});
    public static final Location FALADOR_COWS = new Location("Falador cows", new Area(new Position(3021, 3297, 0), new Position(3042, 3312, 0)), new String[]{"Cow", "Cow calf"});
    public static final Location EDGEVILLE_MEN = new Location("Edgeville men", new Area(new Position(3089, 3504, 0), new Position(3106, 3517, 0)), new String[]{"Man" ,"Woman"});

    public static final Location[] TIER_TWO_TRAINING_AREAS = { LUMBRIDGE_EAST_GOBLINS, LUMBRIDGE_WEST_COWS,
                                LUMBRIDGE_EAST_COWS, FALADOR_COWS, EDGEVILLE_MEN};

    /*
    Levels 20-40
     */

    public static final Location AL_KHARID_WARRIORS = new Location("Al-Kharid warriors", new Area(new Position(3283, 3167, 0), new Position(3303, 3176, 0)), new String[]{"Al-Kharid warrior"});
    public static final Location BARBARIAN_VILLAGE = new Location("Barbarian village barbarians", new Area(new Position(3069, 3402, 0), new Position(3092, 3448, 0)), new String[]{"Barbarian"});
    public static final Location LUMBRIDGE_SWAMP_RATS_FROGS = new Location("Lumbridge swamp", new Area(new Position(3177, 3163, 0), new Position(3229, 3190, 0)), new String[]{"Giant rat", "Big frog", "Giant frog", "Frog"});

    public static final Location[] TIER_THREE_TRAINING_AREAS = { AL_KHARID_WARRIORS, BARBARIAN_VILLAGE, LUMBRIDGE_SWAMP_RATS_FROGS };

    /*
    Levels 40+
     */

    public static final Location HILL_GIANTS = new Location("Hill Giants", new Area(new Position(3090, 9820, 0), new Position(3135, 9865, 0)), new String[]{"Hill Giant"});
    public static final Location FALADOR_GUARDS = new Location("Falador guards", new Area(new Position(2949, 3372, 0), new Position(2977, 3405, 0)), new String[]{"Guard"});

    public static final Location[] TIER_FOUR_TRAINING_AREAS = { AL_KHARID_WARRIORS, HILL_GIANTS, FALADOR_GUARDS, LUMBRIDGE_SWAMP_RATS_FROGS};

    public static final Location[][] TIER_AREAS = { TIER_ONE_TRAINING_AREAS, TIER_TWO_TRAINING_AREAS,
                                                    TIER_THREE_TRAINING_AREAS, TIER_THREE_TRAINING_AREAS, TIER_FOUR_TRAINING_AREAS};

    public static Location getCurrentLocation(CombatTrainer script) {
        script.log("location - tier: " + script.currentTier);
        for(int i = 0; i < TIER_AREAS[script.currentTier - 1].length; i++) {
            Location location = TIER_AREAS[script.currentTier - 1][i];

            if (location.getArea().contains(script.myPlayer())) {
                return location;
            }
        }
        return null;
    }

    public static boolean isInCurrentLocation(CombatTrainer script, Entity entity) {
        if (script.currentLocation.getArea().contains(entity))
            return true;
        return false;
    }
}
