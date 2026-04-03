package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class DefendCommand implements ActionCommand {
    private final ArenaFighter target;
    private final double dodgeBoost;
    private double appliedBoost;

    public DefendCommand(ArenaFighter target, double dodgeBoost) {
        this.target = target;
        this.dodgeBoost = dodgeBoost;
    }

    @Override
    public void execute() {
        double dodgeBefore = target.getDodgeChance();
        target.modifyDodgeChance(dodgeBoost);
        appliedBoost = target.getDodgeChance() - dodgeBefore;
        System.out.printf("[Action] %s takes a defensive stance (+%.2f dodge).%n",
                target.getName(), appliedBoost);
    }

    @Override
    public void undo() {
        if (appliedBoost != 0.0) {
            target.modifyDodgeChance(-appliedBoost);
            appliedBoost = 0.0;
        }
    }

    @Override
    public String getDescription() {
        return String.format("Defend (dodge boost: +%.2f)", dodgeBoost);
    }
}
