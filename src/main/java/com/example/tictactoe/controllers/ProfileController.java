package com.example.tictactoe.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.example.tictactoe.*;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.tictactoe.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

import static com.example.tictactoe.HelloApplication.Playmusic;
import static com.example.tictactoe.HelloApplication.Stopmusic;

public class ProfileController implements Initializable  {

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

    @FXML
    Text CurrentUser;
    @FXML

    Text CurrentScore;

    @FXML
    Text CurrentWin;

    @FXML
    Text CurrentLoss;
@FXML
Button StopMusic;

    @FXML
    Button Playmusic;

@FXML
Button sendBtn;

@FXML
TextField txtF;


//
//    public static  MultiGameController myControllerHandle2;
//


//public void sendMsgForAll(){
//        String msg ;
//        if(txtF.getText() != null ){
//            msg = txtF.getText() ;
//            ClientServerHandler.sendMessageToAll(msg) ;
//        }
//    }
public void StopMusic(ActionEvent stop){
    Stopmusic();
}
public void PlayMusic(ActionEvent Play){
    Playmusic();
}
    public void SwitchToSinglePlayer(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        System.out.println(getClass().getResource("/fxml/difficultyLevel.fxml"));
        root = FXMLLoader.load(getClass().getResource("/fxml/difficultyLevel.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void SwitchToTablePlayer(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        System.out.println(getClass().getResource("/fxml/main.fxml"));
        root = FXMLLoader.load(getClass().getResource("/fxml/TablePlayers.fxml"));
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
        Object myControllerHandle2 = loader.getController();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        HamburgerSlideCloseTransition transition1= new HamburgerSlideCloseTransition(Ham);
//        transition1.setRate(-1);
//        Ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->{
//            transition1.setRate(transition1.getRate() * -1);
//            transition1.play();
//            try {
//                System.out.println("hallo");
//                VBox Tables = FXMLLoader.load(getClass().getResource("/fxml/TablePlayers.fxml"));
//                Drawer.setSidePane(Tables);
//                if(Drawer.isOpened()) {
//                    Drawer.close();
//                    Drawer.setDisable(true);
//                }
//                else {
//                    Drawer.open();
//                    Drawer.setVisible(true);
//                    Drawer.setDisable(false);
//                }
//
//            } catch (IOException ex) {
//                ex.printStackTrace(); }
//             });

        CurrentUser.setText(CurrentPlayerModel.username);
        CurrentScore.setText(CurrentPlayerModel.score);
        CurrentWin.setText(CurrentPlayerModel.wins);
        CurrentLoss.setText(CurrentPlayerModel.losses);


    }

    public void sendToAll(){
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