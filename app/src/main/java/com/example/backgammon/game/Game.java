package com.example.backgammon.game;

import java.util.Observer;

public class Game {
    private String whitePlayer;
    private String blackPlayer;
    private Dice dice;
    private int iii;
    private Board logicBoard;
    public Observer observer;

    public Game(Observer observer){
        this.observer = observer;
    }


}
