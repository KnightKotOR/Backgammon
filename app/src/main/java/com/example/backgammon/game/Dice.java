package com.example.backgammon.game;

import java.util.Random;

public class Dice {
    private int dice1;
    private int dice2;
    private int uses1;
    private int uses2;
    private Random random;

    public void FirstRoll() {
        while (dice1 == dice2) {
            dice1 = random.nextInt(6) + 1;
            dice2 = random.nextInt(6) + 1;
        }
    }

    public void DiceRoll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
        if (dice1 == dice2) uses1 = uses2 = 2;
        else uses1 = uses2 = 1;
    }

    public void DiceUsed(int n) {
        if (uses1 > 0 && dice1 == n) uses1--;
        else if (uses2 > 0 && dice2 == n) uses2--;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }
}
