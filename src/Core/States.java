package Core;

public enum States {
    STARTING_UP("Starting up..."),
    CHECK_ITEMS("Banking: checking state of items"),
    PURCHASE_ITEMS("Buying equipment for next tier"),
    PURCHASE_FOOD("Buying food"),
    EQUIP_GEAR("Equipping items"),
    WALK_TO_COMBAT("Walking to combat area"),
    FIGHT("Fighting"),
    BANKING_FOR_FOOD("Banking for food"),
    WITHDRAW_FOOD("Withdrawing food"),
    UPGRADING_GEAR("Upgrading gear for next tier"),
    EAT("Eating"),
    BRASS_KEY("Getting Brass key");


    private final String string;

    States(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
