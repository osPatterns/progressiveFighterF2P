package tasks;

import Core.CombatTrainer;

public abstract class Task {

    protected CombatTrainer script;

    public Task(CombatTrainer script) {
        this.script = script;
    }

    public abstract boolean canProcess() throws InterruptedException;

    public abstract void process() throws InterruptedException;

    public void run() throws InterruptedException {
        if (canProcess())
            process();
    }
} 