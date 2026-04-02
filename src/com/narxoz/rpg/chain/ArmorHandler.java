package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class ArmorHandler extends DefenseHandler {
    private final int armorValue;

    public ArmorHandler(int armorValue) {
        this.armorValue = armorValue;
    }

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        int absorbed = Math.max(0, Math.min(incomingDamage, armorValue));
        int remainder = Math.max(0, incomingDamage - armorValue);

        System.out.println("[Armor] Armor absorbs " + absorbed + " damage.");
        passToNext(remainder, target);
    }
}
