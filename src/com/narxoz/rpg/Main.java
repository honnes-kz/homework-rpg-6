package com.narxoz.rpg;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 6 Demo: Chain of Responsibility + Command ===\n");

        // -----------------------------------------------------------------------
        // Part 1 - Command Queue Demo
        // -----------------------------------------------------------------------
        System.out.println("--- Command Queue Demo ---");

        ArenaFighter queueHero = new ArenaFighter("Aruzhan", 100, 0.18, 25, 5, 18, 3);
        ArenaOpponent queueOpponent = new ArenaOpponent("Arena Wolf", 70, 12);
        queueHero.takeDamage(22);

        ActionQueue queue = new ActionQueue();
        queue.enqueue(new AttackCommand(queueOpponent, queueHero.getAttackPower()));
        queue.enqueue(new HealCommand(queueHero, 20));
        queue.enqueue(new DefendCommand(queueHero, 0.15));

        System.out.println("Before commands -> "
                + queueHero.getName() + " HP: " + queueHero.getHealth()
                + ", potions: " + queueHero.getHealPotions()
                + ", dodge: " + String.format("%.2f", queueHero.getDodgeChance()));
        System.out.println("Queued actions:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        System.out.println("\nUndoing last queued action...");
        queue.undoLast();

        System.out.println("Queue after undo:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        queue.enqueue(new DefendCommand(queueHero, 0.15));
        System.out.println("\nExecuting all queued commands...");
        queue.executeAll();
        System.out.println("After commands -> "
                + queueOpponent.getName() + " HP: " + queueOpponent.getHealth()
                + " | " + queueHero.getName() + " HP: " + queueHero.getHealth()
                + ", potions: " + queueHero.getHealPotions()
                + ", dodge: " + String.format("%.2f", queueHero.getDodgeChance()));

        // -----------------------------------------------------------------------
        // Part 2 - Defense Chain Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Defense Chain Demo ---");

        ArenaFighter chainHero = new ArenaFighter("Temir", 110, 0.20, 30, 5, 20, 2);
        DefenseHandler dodge = new DodgeHandler(chainHero.getDodgeChance(), 42L);
        DefenseHandler block = new BlockHandler(chainHero.getBlockRating() / 100.0);
        DefenseHandler armor = new ArmorHandler(chainHero.getArmorValue());
        DefenseHandler hp = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Sending 20 incoming damage through the defense chain...");
        System.out.println("Hero HP before: " + chainHero.getHealth());
        dodge.handle(20, chainHero);
        System.out.println("Hero HP after:  " + chainHero.getHealth());

        // -----------------------------------------------------------------------
        // Part 3 - Full Tournament Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Full Arena Tournament ---");

        ArenaFighter tournamentHero = new ArenaFighter("Erlan", 120, 0.25, 25, 8, 22, 3);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Iron Vane", 100, 16);

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent)
                .setRandomSeed(42L)
                .runTournament();

        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println("  " + line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
