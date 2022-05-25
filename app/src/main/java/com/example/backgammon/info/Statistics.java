package com.example.backgammon.info;

public class Statistics {
    private String name;
    private Integer games;
    private Integer wins;
    private Integer loses;
    private String percent;

    public Statistics(){}

    public Statistics(String name, Integer games, Integer wins){
        this.name = name;
        this.games = games;
        this.wins = wins;
        this.loses = games - wins;
        this.percent = String.valueOf(wins/games);
    }

    public String getName() {
        return name;
    }

    public Integer getGames() {
        return games;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getLoses() {
        return loses;
    }

    public String getPercent() {
        return percent;
    }
}
