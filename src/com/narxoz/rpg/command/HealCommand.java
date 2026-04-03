package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class HealCommand implements ActionCommand {
    private final ArenaFighter target;
    private final int healAmount;
    private int actualHealApplied;

    public HealCommand(ArenaFighter target, int healAmount) {
        this.target = target;
        this.healAmount = healAmount;
    }

    @Override
    public void execute() {
        actualHealApplied = 0;

        if (target.getHealPotions() <= 0) {
            System.out.println("[Action] Heal skipped. No potions left for " + target.getName() + ".");
            return;
        }

        int healthBefore = target.getHealth();
        target.heal(healAmount);
        actualHealApplied = target.getHealth() - healthBefore;

        if (actualHealApplied > 0) {
            System.out.println("[Action] " + target.getName() + " heals for " + actualHealApplied + " HP.");
        } else {
            System.out.println("[Action] Heal skipped. " + target.getName() + " is already at full health.");
        }
    }

    @Override
    public void undo() {
        if (actualHealApplied > 0) {
            target.takeDamage(actualHealApplied);
            actualHealApplied = 0;
        }
    }

    @Override
    public String getDescription() {
        return "Heal " + target.getName() + " for up to " + healAmount + " HP";
    }
}
