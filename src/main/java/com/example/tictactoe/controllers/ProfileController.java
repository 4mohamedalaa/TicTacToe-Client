package com.example.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.tictactoe.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    Label username;
    @FXML
    Button singlePlayer;
    @FXML
    Button multiplayer;
    @FXML
    Button logout;

    public void SwitchToSinglePlayer(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        System.out.println(getClass().getResource("/fxml/main.fxml"));
        root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void SwitchToMultiplayer(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/multiPlayer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        // Parent root;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/starter.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ClientServerHandler.signOut();
    }

    public void DisplayUsername(String name) {
        username.setText(name);
    }

}