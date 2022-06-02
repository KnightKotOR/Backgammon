package com.example.backgammon.logic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int baseWhite;
    private int baseBlack;
    private int barWhite;
    private int barBlack;
    private int countWhite;
    private int countBlack;
    private static final List<Checker> board = new ArrayList<>(24);

    public Board() {
        init();
    }

    public void init() {
        baseWhite = 0;
        baseBlack = 0;
        barWhite = 0;
        barBlack = 0;
        countWhite = 15;
        countBlack = 15;
        board.add(new Checker(1, 15));
        for (int i = 1; i < 24; i++) {
            if (i != 12) {
                board.add(new Checker(0, 0));
            } else board.add(new Checker(2, 15));
        }
    }

    public boolean canMove(int from, int target, int playerColor, int dice1, int dice2) {
        int color = board.get(from).getColor();

        int step = target - from;
        int targetColor = board.get(target).getColor();
        if ((playerColor == 1 && from < 24 && from > 17 && target > 0 && target < 6) ||
                (playerColor == 2 && from < 12 && from > 5 && target > 11 && target < 18)){
            return false;
        }
        return ((playerColor == color) && (targetColor == 0 || targetColor == color) &&
                (step == dice1 || step == dice2 || dice1 - step == 24 || dice2 - step == 24));
    }

    public boolean canPut(int from, int playerColor, int dice) {
        int color = board.get(from).getColor();
        int step = from + dice;
        int n;

        if (playerColor == 1 && baseWhite == countWhite) {
            n = 24;
        } else if (playerColor == 2 && baseBlack == countBlack) {
            n = 12;
        } else return false;

        return (color == playerColor) && (step == n) && (step > n) && previousFree(from, playerColor);
    }

    private boolean previousFree(int from, int color) {
        int i;
        if (color == 1) i = 18;
        else i = 6;
        while (i < from) {
            if (board.get(i).getColor() == color) return false;
            i++;
        }
        return true;
    }

    public void move(int from, int target) {
        int count = board.get(from).getCount();
        int color = board.get(from).getColor();
        int targetCount = board.get(target).getCount();
        if (count == 1) {
            board.set(from, new Checker(0, 0));
        } else board.get(from).changeCount(count - 1);
        if (targetCount == 0) {
            board.set(target, new Checker(color, 1));
        } else board.get(target).changeCount(targetCount + 1);
        isInBase(target, color);
    }

    public void put(int from) {
        int color = board.get(from).getColor();
        int count = board.get(from).getCount();
        if (count == 1) {
            board.set(from, new Checker(0, 0));
        } else {
            board.get(from).changeCount(count - 1);
        }
        if (color == 1) {
            barWhite++;
            baseWhite--;
            countWhite--;
        } else {
            barBlack++;
            baseBlack--;
            countBlack--;
        }
    }

    private void isInBase(int target, int color) {
        if ((color == 1 && target >= 18)) {
            baseWhite++;

        } else if (color == 2 && target >= 6) {
            baseBlack++;
        }
    }

    public int getBarBlack() {
        return barBlack;
    }

    public int getBarWhite() {
        return barWhite;
    }

    public List<Checker> getBoard() {
        return board;
    }

    public Map<Integer, Integer> getPossibleMoves(int playerColor, int dice1, int dice2) {
        Map<Integer, Integer> possibleMoves = new HashMap<>();
        /*
        for (int from = 0; from < 24; from++) {
            for (int target = 1; target < 30; target++) {
                if (canMove(from, target, playerColor, dice1, dice2) || canPut(from, playerColor, dice1) || canPut(from, playerColor, dice2)) {
                    possibleMoves.put(from, target);
                }
            }
        }
         */

        for(int from = 0; from < 24; from++){
            int target = from + dice1;
            if (target > 23 && playerColor == 2) target-=24;
            if (canMove(from,target,playerColor,dice1,dice2)){
                possibleMoves.put(from,target);
            }
            target = from + dice2;
            if (target > 23 && playerColor == 2) target-=24;
            if (canMove(from,target,playerColor,dice1,dice2)){
                possibleMoves.put(from,target);
            }
        }
        return possibleMoves;
    }
}
