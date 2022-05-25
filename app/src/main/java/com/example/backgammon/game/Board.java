package com.example.backgammon.game;


import java.util.HashMap;
import java.util.Map;

public class Board {
    private int baseWhite;
    private int baseBlack;
    private int barWhite;
    private int barBlack;
    private int countWhite;
    private int countBlack;
    private final Map<Integer, Checker> board = new HashMap<>();

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
        board.put(0, new Checker(1, 15));
        board.put(23, new Checker(2, 15));
        for (int i = 1; i < 23; i++) {
            board.put(i, new Checker(0, 0));
        }
    }

    public boolean canMove(int from, int step) {
        int count = board.get(from).getCount();
        if (count == 0) {
            return false;
        }
        int color = board.get(from).getColor();
        int target = from + step;
        target = targetCorrect(target, color);
        int targetColor = board.get(target).getColor();
        if (targetColor == 0 || targetColor == color)
            return true;
        return false;
    }

    public boolean canPut(int from, int step, int color) {
        int target = from + step;
        target = targetCorrect(target, color);
        if ((color == 1 && baseWhite == countWhite) || (color == 2 && baseBlack == countBlack)) {
            if (target == 24 || (target > 24 && previousFree(from, color))) {
                return true;
            }
        }
        return false;
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

    public void move(int from, int step) {
        if (!canMove(from, step)) throw new IllegalArgumentException();
        int count = board.get(from).getCount();
        int color = board.get(from).getColor();
        int target = from + step;
        target = targetCorrect(target, color);
        int targetCount = board.get(target).getCount();
        if (count == 1) {
            board.put(from, new Checker(0, 0));
        } else board.get(from).changeCount(count - 1);
        if (targetCount == 0) {
            board.put(target, new Checker(color, 1));
        } else board.get(from).changeCount(targetCount + 1);
        isInBase(target, color);
    }

    public void put(int from, int step) {
        int color = board.get(from).getColor();
        if (!canPut(from, step, color)) throw new IllegalArgumentException();
        int count = board.get(from).getCount();
        int target = from + step;
        target = targetCorrect(target, color);
        if (count == 1) {
            board.put(from, new Checker(0, 0));
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

    private int targetCorrect(int target, int color) {
        if (color == 2 && target > 23) {
            return target - 24;
        }
        return target;
    }

    public Map<Integer, Checker> getBoard() {
        return board;
    }
}
