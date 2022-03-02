package com.example.tictactoe.models;

import com.example.tictactoe.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class PausedGame {
    private Integer game_id;
    private Integer opponentId;
    private String opponent;
    private Button InviteBtn;
    public String getOpponent() {
        return opponent;
    }
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
    public Button getInviteBtn() {
        return InviteBtn;
    }
    public void setInviteBtn(Button inviteBtn) {
        InviteBtn = inviteBtn;
    }
    public Integer getGame_id() {
        return game_id;
    }
    public void setGame_id(Integer game_id) {
        this.game_id = game_id;
    }
    public Integer getOpponentId() {
        return opponentId;
    }
    public void setOpponentId(Integer opponentId) {
        this.opponentId = opponentId;
    }


    public PausedGame(){
        this.game_id = 0;
        this.opponent = "";
        this.opponentId = 0;
        this.InviteBtn = null;
    }

    public PausedGame(Integer game_id, String opponent, Integer opponentId ){
        this.game_id = game_id;
        this.opponent = opponent;
        this.opponentId = opponentId;
        this.InviteBtn = new Button("invite");

    }

    public void setInviteButtonHandler(){
        this.InviteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientServerHandler.sendInvitation(getOpponentId());
            }
        });
    }
}
