package com.example.backgammon.logic;

import java.util.Random;

public class Dice {
    private int dice1;
    private int dice2;
    private static int uses1;
    private static int uses2;
    public boolean jackpot;
    private Random random = new Random();

    public void FirstRoll() {
        while (dice1 == dice2) {
            dice1 = random.nextInt(6) + 1;
            dice2 = random.nextInt(6) + 1;
        }
    }

    public void diceRoll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
        if (dice1 == dice2) {
            uses1 = uses2 = 2;
            jackpot = true;
        }
        else {
            uses1 = uses2 = 1;
            jackpot = false;
        }
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public int getUses1() {
        return uses1;
    }

    public int getUses2() {
        return uses2;
    }
}
