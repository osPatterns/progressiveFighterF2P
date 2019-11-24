package Core;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.canvas.paint.Painter;

import java.awt.*;

class Paint implements Painter {

    private CombatTrainer combatTrainer;

    private boolean hidden;

    public Paint(CombatTrainer combatTrainer) {
        this.combatTrainer = combatTrainer;

        startTime = System.currentTimeMillis();
        combatLevelStart = combatTrainer.getCombat().getCombatLevel();

        combatTrainer.getExperienceTracker().start(Skill.ATTACK);
        combatTrainer.getExperienceTracker().start(Skill.STRENGTH);
        combatTrainer.getExperienceTracker().start(Skill.DEFENCE);
        combatTrainer.getExperienceTracker().start(Skill.HITPOINTS);
    }

    public static Font font = new Font("Open Sans", Font.BOLD, 14);

    private long startTime;
    private int combatLevelStart;

    @Override
    public void onPaint(Graphics2D g) {
        if (hidden) {
            //g.drawImage(img2, 490, 346, null);
            return;
        }

        g.drawString("Patterns' F2P Combat Trainer", 30, 30);
        g.setColor(Color.WHITE);
        Point mP = combatTrainer.getMouse().getPosition();

        g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
        g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

        g.drawString("Patterns' F2P Combat Trainer", 30, 30);

        g.drawString("State: " + combatTrainer.currentState.getString(), 30, 45);
        String location = (combatTrainer.currentLocation == null) ? "Not yet determined" : combatTrainer.currentLocation.getName();
        g.drawString("Current training location: " + location, 30, 60);
        g.drawString("Runtime: " + Paint.formatTime(System.currentTimeMillis() - startTime), 30, 75);

        g.drawString("Levels: Start/Current/XP PH", 30, 105);
        g.drawString("Attack level: " + (combatTrainer.getSkills().getStatic(Skill.ATTACK) - combatTrainer.getExperienceTracker().getGainedLevels(Skill.ATTACK)) + "/" + combatTrainer.getSkills().getStatic(Skill.ATTACK) + " (" + combatTrainer.getExperienceTracker().getGainedXPPerHour(Skill.ATTACK) + ")", 30, 120);
        g.drawString("Strength level: " + (combatTrainer.getSkills().getStatic(Skill.STRENGTH) - combatTrainer.getExperienceTracker().getGainedLevels(Skill.STRENGTH)) + "/" + combatTrainer.getSkills().getStatic(Skill.STRENGTH) + " (" + combatTrainer.getExperienceTracker().getGainedXPPerHour(Skill.STRENGTH) + ")", 30, 135);
        g.drawString("Defence level: " + (combatTrainer.getSkills().getStatic(Skill.DEFENCE) - combatTrainer.getExperienceTracker().getGainedLevels(Skill.DEFENCE)) + "/" + combatTrainer.getSkills().getStatic(Skill.DEFENCE) + " (" + combatTrainer.getExperienceTracker().getGainedXPPerHour(Skill.DEFENCE) + ")", 30, 150);
        g.drawString("Combat level: " + combatLevelStart + "/" + combatTrainer.getCombat().getCombatLevel(), 30, 165);
    }

    public static final String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }

    public static final String formatValue(final long l) {
        return (l > 1_000_000) ? String.format("%.2fm", ((double) l / 1_000_000))
                : (l > 1000) ? String.format("%.1fk", ((double) l / 1000))
                : l + "";
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
