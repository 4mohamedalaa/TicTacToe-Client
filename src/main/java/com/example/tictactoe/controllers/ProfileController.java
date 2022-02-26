package com.example.tictactoe.controllers;

import com.example.tictactoe.models.CurrentPlayerModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.tictactoe.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable  {
    @FXML
    Button sendMsg;
    @FXML
    TextField txtF;
    @FXML
    public TextArea txtA;
    @FXML
    Label username;
    @FXML
    Button singlePlayer;
    @FXML
    Button multiplayer;
    @FXML
    Button logout;
    public static  MainGameController myControllerHandle2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
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
        Stage stage ;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/multiPlayer.fxml"));
        root = loader.load();
        myControllerHandle2 = loader.getController();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        /*
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/multiPlayer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/

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
    public void sendToAll( ){
        //System.out.println("clicked");
               String msg = txtF.getText();
                if(msg != null ){
                   // System.out.println("inside clicked ");
                    System.out.println(CurrentPlayerModel.username);
                    System.out.println(msg);
                    ClientServerHandler.sendMessageForAll(msg, CurrentPlayerModel.username);
                }
    }

}