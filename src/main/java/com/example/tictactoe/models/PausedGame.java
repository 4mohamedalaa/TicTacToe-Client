package com.example.tictactoe.models;

import com.example.tictactoe.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class PausedGame {
    private Integer game_id;
    private Integer opponentId;
    private String opponentName;
    private Button resumeBtn;


    public PausedGame(){
        this.game_id = 0;
        this.opponentName = "";
        this.opponentId = 0;
        this.resumeBtn = new Button("resume");;
    }

    public PausedGame(Integer game_id, String opponentName, Integer opponentId ){
        this.game_id = game_id;
        this.opponentName = opponentName;
        this.opponentId = opponentId;
        this.resumeBtn = new Button("invite");

    }

    public void setResumeButtonHandler(){
        this.resumeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientServerHandler.sendResumeInvitation(getOpponentId(),getGame_id());
                System.out.println("Send resume for game "+getGame_id()+" to "+ getOpponentId());
            }
        });
    }

    public String getOpponentName() {
        return opponentName;
    }
    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
    public Button getResumeBtn() {
        return resumeBtn;
    }
    public void setInviteBtn(Button inviteBtn) {
        resumeBtn = inviteBtn;
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

}
