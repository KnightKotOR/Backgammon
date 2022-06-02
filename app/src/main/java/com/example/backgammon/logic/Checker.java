package com.example.backgammon.logic;

public class Checker {
    private Integer count;
    private Integer color;

    Checker(Integer color, Integer count) {
        this.count = count;
        this.color = color;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getColor() {
        return color;
    }

    public void changeCount(int newCount) {
        this.count = newCount;
    }
}
