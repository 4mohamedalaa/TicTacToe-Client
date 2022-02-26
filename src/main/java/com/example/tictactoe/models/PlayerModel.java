package com.example.tictactoe.models;

import javafx.scene.control.Button;

import java.util.EventListener;

public class PlayerModel {
    private Integer id;
    private String username;
    private Integer wins;
    private Integer losses;
    private Integer score;
    private boolean online = false;
    private String hashedPassword;
    private Button InviteBtn;

    public PlayerModel(){
        this.id = 0;
        this.username = "";
        this.online = false;
        this.score = 0;
        this.wins = 0;
        this.losses = 0;
        this.InviteBtn=new Button("Invite");
    }

    public PlayerModel(Integer playerId, String playerUsername, Integer playerScore){
        this.id = playerId;
        this.score = playerScore;
        this.username = playerUsername;
        this.online = false;
        this.losses = 0;
        this.wins = 0;
    }

    public PlayerModel(Integer playerId, Integer playerScore, String playerUsername, boolean playerStatus){
        this.id = playerId;
        this.score = playerScore;
        this.username = playerUsername;
        this.online = playerStatus;
        this.losses = 0;
        this.wins = 0;
    }

    public PlayerModel(Integer playerId, Integer playerScore, String playerUsername, boolean playerStatus, Integer playerWins, Integer playerLosses){
        this.id = playerId;
        this.score = playerScore;
        this.username = playerUsername;
        this.online = playerStatus;
        this.losses = playerLosses;
        this.wins = playerWins;
    }

    // Setters & Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setInviteBtn(Button button){this.InviteBtn = button; }

    public Button getInviteBtn(){return InviteBtn;}
}
