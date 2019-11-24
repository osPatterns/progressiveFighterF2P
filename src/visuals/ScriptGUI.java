package visuals;

import Core.CombatTrainer;
import Core.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScriptGUI {

    private CombatTrainer script;

    private final JDialog mainDialog;

    private final String title = "Patterns's AIO F2P Combat Trainer";

    private boolean trainAttack = true;
    private boolean trainStrength = true;
    private boolean trainDefence = true;
    private boolean upgradeGear = true;

    private boolean ready;

    public ScriptGUI(CombatTrainer script) {
        this.script = script;

        mainDialog = new JDialog();
        mainDialog.setTitle(title);
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainDialog.getContentPane().add(mainPanel);

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //checkBoxPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JCheckBox trainAttackCheckBox = new JCheckBox("Train Attack?", true);
        trainAttackCheckBox.addActionListener(e -> {
            trainAttack = trainAttackCheckBox.isSelected();
        });

        checkBoxPanel.add(trainAttackCheckBox);
        JCheckBox trainStrengthCheckBox = new JCheckBox("Train Strength?", true);
        trainStrengthCheckBox.addActionListener(e -> {
            trainStrength = trainStrengthCheckBox.isSelected();
        });

        checkBoxPanel.add(trainStrengthCheckBox);
        JCheckBox trainDefenceCheckBox = new JCheckBox("Train Defence?", true);
        trainDefenceCheckBox.addActionListener(e -> {
            trainDefence = trainDefenceCheckBox.isSelected();
        });

        checkBoxPanel.add(trainDefenceCheckBox);

        mainPanel.add(checkBoxPanel, BorderLayout.NORTH);

        JPanel gearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //gearPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JCheckBox upgradeGearCheckBox = new JCheckBox("Automatically upgrade gear?", true);
        upgradeGearCheckBox.addActionListener(e -> {
            upgradeGear = upgradeGearCheckBox.isSelected();
        });

        gearPanel.add(upgradeGearCheckBox);

        mainPanel.add(gearPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startButton = new JButton("Start");

        startButton.addActionListener(e -> {
            initializeSettings();
            close();
        });

        buttonPanel.add(startButton);
        mainPanel.add(buttonPanel);

        mainDialog.pack();
        mainDialog.setResizable(false);
        mainDialog.setLocationRelativeTo(null);
    }

    private void initializeSettings() {
        if (!trainAttack && !trainStrength && !trainDefence) {
            JOptionPane popup = new JOptionPane();
            popup.createDialog("You need to train at least one skill!");
            return;
        }

        script.settings = new Settings("Trout", upgradeGear, trainAttack, trainStrength, trainDefence);
        ready = true;
    }

    public void open() {
        mainDialog.setVisible(true);
    }

    public void close() {
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

    public boolean isReady() {
        return ready;
    }
}
