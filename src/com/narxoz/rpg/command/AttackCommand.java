package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaOpponent;

public class AttackCommand implements ActionCommand {
    private final ArenaOpponent target;
    private final int attackPower;
    private int damageDealt;

    public AttackCommand(ArenaOpponent target, int attackPower) {
        this.target = target;
        this.attackPower = attackPower;
    }

    @Override
    public void execute() {
        int healthBefore = target.getHealth();
        target.takeDamage(attackPower);
        damageDealt = healthBefore - target.getHealth();
        System.out.println("[Action] Attack hits " + target.getName() + " for " + damageDealt + " damage.");
    }

    @Override
    public void undo() {
        if (damageDealt > 0) {
            target.restoreHealth(damageDealt);
            damageDealt = 0;
        }
    }

    @Override
    public String getDescription() {
        return "Attack " + target.getName() + " for " + attackPower + " damage";
    }
}
