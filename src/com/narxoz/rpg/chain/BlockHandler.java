package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class BlockHandler extends DefenseHandler {
    private final double blockPercent;

    public BlockHandler(double blockPercent) {
        this.blockPercent = blockPercent;
    }

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        int blocked = Math.max(0, Math.min(incomingDamage, (int) (incomingDamage * blockPercent)));
        int remainder = Math.max(0, incomingDamage - blocked);

        System.out.println("[Block] Shield blocks " + blocked + " damage.");
        passToNext(remainder, target);
    }
}
