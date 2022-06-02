package com.example.backgammon.logic;

import java.util.Map;

public class Game {
    public Board logicBoard;
    public Dice logicDice;
    public int barWhite, barBlack;
    public Integer dice1, dice2, uses1, uses2;
    private final int playerWhite, playerBlack;
    public int curTurn;
    public boolean win, firstTaken;
    private int turnCounter;
    public Integer from, target;
    public Map<Integer, Integer> possibleMoves;
    public int step;

    public Game() {
        logicBoard = new Board();
        logicDice = new Dice();
        playerWhite = 1;
        playerBlack = 2;
        turnCounter = 1;
        barWhite = logicBoard.getBarWhite();
        barBlack = logicBoard.getBarBlack();
        win = false;
        firstTaken = false;

        start();
        roll();
        possibleMoves = logicBoard.getPossibleMoves(curTurn, dice1, dice2);


    }


    private void start() {
        logicBoard.init();
        logicDice.FirstRoll();
        dice1 = logicDice.getDice1();
        dice2 = logicDice.getDice2();
        if (dice1 > dice2) {
            curTurn = playerWhite;
        } else {
            curTurn = playerBlack;
        }
    }

    public void prepareMove(int curTurn) {
        if (curTurn == playerWhite) this.curTurn = playerBlack;
        else this.curTurn = playerWhite;
        firstTaken = false;
        turnCounter++;
        possibleMoves = logicBoard.getPossibleMoves(this.curTurn, dice1, dice2);
    }

    public void roll() {
        logicDice.diceRoll();
        dice1 = logicDice.getDice1();
        dice2 = logicDice.getDice2();
        uses1 = logicDice.getUses1();
        uses2 = logicDice.getUses2();
    }

    public void playerMove(int from, int target) {
        step = target - from;

        if ((curTurn == playerWhite && from == 0) || (curTurn == playerBlack && from == 12)) {
            if (!firstTaken) {
                firstTaken = true;
            } else if (logicDice.jackpot && (dice1 == 3 || dice1 == 4 || dice1 == 6) && (turnCounter == 1 || turnCounter == 2))
                logicDice.jackpot = false;
            else return;
        }

        logicBoard.move(from, target);
        if (dice1 - step == 24){
            diceUsed(dice1);
        }
        else if (dice2 - step == 24){
            diceUsed(dice2);
        }
        else diceUsed(step);
        possibleMoves = logicBoard.getPossibleMoves(curTurn, dice1, dice2);
    }

    public void playerPut(int from) {
        if (logicBoard.canPut(from, curTurn, dice1)) {
            step = dice1;
        } else if (logicBoard.canPut(from, curTurn, dice2)) {
            step = dice2;
        } else return;
        logicBoard.put(from);
        checkWin(curTurn);
        diceUsed(step);
    }

    public void checkWin(int player) {
        if ((player == playerWhite && barWhite == 15) || (player == playerBlack && barBlack == 15)) {
            win = true;
        }
    }


    public void diceUsed(int step) {
        if (step == dice1) uses1--;
        else if (step == dice2) uses2--;
        if (uses1 == 0) {
            dice1 = 0;
        } else if (uses2 == 0) {
            dice2 = 0;
        }
    }

    public Board getLogicBoard() {
        return this.logicBoard;
    }

}
