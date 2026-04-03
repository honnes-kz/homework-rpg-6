package com.narxoz.rpg.tournament;

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
import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;
        final int healAmount = 15;
        final double dodgeBoost = 0.10;
        ActionQueue actionQueue = new ActionQueue();

        while (hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;
            System.out.println("\n[Round " + round + "]");

            AttackCommand attackCommand = new AttackCommand(opponent, hero.getAttackPower());
            HealCommand healCommand = new HealCommand(hero, healAmount);
            DefendCommand defendCommand = new DefendCommand(hero, dodgeBoost);

            actionQueue.enqueue(attackCommand);
            actionQueue.enqueue(healCommand);
            actionQueue.enqueue(defendCommand);

            String queueLine = "[Round " + round + "] Queue: "
                    + String.join(", ", actionQueue.getCommandDescriptions());
            System.out.println(queueLine);
            result.addLine(queueLine);

            actionQueue.executeAll();

            String heroTurnLine = "[Round " + round + "] After hero turn -> "
                    + opponent.getName() + " HP: " + opponent.getHealth()
                    + " | " + hero.getName() + " HP: " + hero.getHealth()
                    + " | Potions: " + hero.getHealPotions();
            System.out.println(heroTurnLine);
            result.addLine(heroTurnLine);

            if (opponent.isAlive()) {
                DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), random.nextLong());
                DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
                DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
                DefenseHandler hp = new HpHandler();
                dodge.setNext(block).setNext(armor).setNext(hp);

                String attackLine = "[Round " + round + "] " + opponent.getName()
                        + " attacks for " + opponent.getAttackPower() + ".";
                System.out.println(attackLine);
                result.addLine(attackLine);

                dodge.handle(opponent.getAttackPower(), hero);
            } else {
                String defeatLine = "[Round " + round + "] " + opponent.getName()
                        + " was defeated before striking back.";
                System.out.println(defeatLine);
                result.addLine(defeatLine);
            }

            defendCommand.undo();

            String endRoundLine = "[Round " + round + "] End -> "
                    + opponent.getName() + " HP: " + opponent.getHealth()
                    + " | " + hero.getName() + " HP: " + hero.getHealth();
            System.out.println(endRoundLine);
            result.addLine(endRoundLine);
        }

        if (hero.isAlive() && opponent.isAlive()) {
            String safeguardLine = "Max rounds reached. Winner decided by remaining HP.";
            System.out.println(safeguardLine);
            result.addLine(safeguardLine);
            result.setWinner(hero.getHealth() >= opponent.getHealth() ? hero.getName() : opponent.getName());
        } else {
            result.setWinner(hero.isAlive() ? hero.getName() : opponent.getName());
        }

        result.setRounds(round);
        return result;
    }
}
