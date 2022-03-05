package com.example.tictactoe.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.tictactoe.HelloApplication.Playmusic;
import static com.example.tictactoe.HelloApplication.Stopmusic;

public class Difficulty implements Initializable {
    static String aiType;
    @FXML
    private Button dump;
    @FXML
    private Button smart;
    @FXML
    private Button unbeatable;
    @FXML
    ImageView logo;
    @FXML
    ImageView o1;
    @FXML
    ImageView o2;
    @FXML
    ImageView o3;
    @FXML
    ImageView x1;
    @FXML
    ImageView x2;
    @FXML
    ImageView x3;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moveX(logo, -90);
        moveY(o1, 50);
        moveY(o2, 100);
        moveY(o3, 200);
        moveY(x1, 50);
        moveY(x2, 100);
        moveY(x3, 200);

        RotateTransition rotate = new RotateTransition();
        rotate.setNode(logo);
        rotate.setDuration(Duration.millis(1000)); // do the rotate in 1 sec
        rotate.setInterpolator(Interpolator.LINEAR); // smooth rotation
        rotate.setByAngle(360);
        rotate.play();
    }
    public void moveY(Node node, int valueY) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        // translate.setByX(valueX); // or 250
        translate.setByY(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your original position after translation
        translate.play();
    }

    public void moveX(Node node, int valueX) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        translate.setByX(valueX); // or 250
        // translate.setByX(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your original position after translation
        translate.play();
    }
}
