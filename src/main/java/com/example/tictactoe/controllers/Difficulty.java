package com.example.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.tictactoe.HelloApplication.Playmusic;
import static com.example.tictactoe.HelloApplication.Stopmusic;

public class Difficulty {
    static String aiType;
    @FXML
    private Button dump;
    @FXML
    private Button smart;
    @FXML
    private Button unbeatable;

    public static String getAiType() {
        return  aiType;
    }


    private void setType(String S) {
        switch (S){
            case "dump":
                aiType = "Dump";
                break;
            case "smart":
                aiType = "Smart";
                break;
            case "unbeatable":
                aiType = "Unbeatable";
                break;
        }
    }

    public void SwitchToSinglePlayer(ActionEvent event) throws IOException {

        Stage stage;
        Scene scene;
        Parent root;
        final Node source = (Node) event.getSource();
        String id = source.getId();
        System.out.println(id);
        setType(id);
        System.out.println(getClass().getResource("/fxml/main.fxml"));
        root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void StopMusic(ActionEvent stop){
        Stopmusic();
    }
    public void PlayMusic(ActionEvent Play){
        Playmusic();
    }
public void BackBtn(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
}
}
