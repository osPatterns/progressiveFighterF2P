package Core;

public class Settings {

    private String food;

    private boolean upgradingGear;

    private boolean trainingAttack;
    private boolean trainingStrength;
    private boolean trainingDefence;

    public Settings(String food, boolean gearUpgrades, boolean trainingAttack, boolean trainingStrength, boolean trainingDefence) {
        this.food = food;
        this.upgradingGear = gearUpgrades;
        this.trainingAttack = trainingAttack;
        this.trainingStrength = trainingStrength;
        this.trainingDefence = trainingDefence;
    }

    public String getFood() {
        return food;
    }

    public boolean upgradeGear() {
        return upgradingGear;
    }

    public boolean isTrainingAttack() {
        return trainingAttack;
    }

    public boolean isTrainingStrength() {
        return trainingStrength;
    }

    public boolean isTrainingDefence() {
        return trainingDefence;
    }

}

