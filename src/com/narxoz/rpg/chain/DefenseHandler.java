package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public abstract class DefenseHandler {
    private DefenseHandler next;

    public DefenseHandler setNext(DefenseHandler next) {
        this.next = next;
        return next;
    }

    protected DefenseHandler getNext() {
        return next;
    }

    protected void passToNext(int damage, ArenaFighter target) {
        if (next != null) {
            next.handle(Math.max(0, damage), target);
        }
    }

    public abstract void handle(int incomingDamage, ArenaFighter target);
}
