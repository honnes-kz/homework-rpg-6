package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class HpHandler extends DefenseHandler {

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        int landedDamage = Math.max(0, incomingDamage);
        target.takeDamage(landedDamage);
        System.out.println("[HP] " + target.getName() + " takes " + landedDamage + " damage.");
    }
}
