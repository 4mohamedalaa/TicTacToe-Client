package com.example.tictactoe.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.example.tictactoe.ClientServerHandler;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;


public class ProfileController extends Application implements Initializable {

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
Button sendBtn;

@FXML
TextField txtF;

@FXML
JFXDrawer Drawer;

@FXML
JFXHamburger Ham;

@FXML
TableView   OnlinePlayers;

@FXML
TableView OfflinePlayers;

    @Override
public void init(){

}



//public void sendMsgForAll(){
//        String msg ;
//        if(txtF.getText() != null ){
//            msg = txtF.getText() ;
//            ClientServerHandler.sendMessageToAll(msg) ;
//        }
//    }

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
        root = FXMLLoader.load(getClass().getResource("/fxml/TablePlayers.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HamburgerSlideCloseTransition transition1= new HamburgerSlideCloseTransition(Ham);
        transition1.setRate(-1);
        Ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->{
            transition1.setRate(transition1.getRate() * -1);
            transition1.play();
            try {
                System.out.println("hallo");
                VBox Tables = FXMLLoader.load(getClass().getResource("/fxml/TablePlayers.fxml"));
                Drawer.setSidePane(Tables);
                if(Drawer.isOpened()) {
                    Drawer.close();
                    Drawer.setDisable(true);
                }
                else {
                    Drawer.open();
                    Drawer.setVisible(true);
                    Drawer.setDisable(false);
                }

            } catch (IOException ex) {
                ex.printStackTrace(); }
             });

        CurrentUser.setText(CurrentPlayerModel.username);
        CurrentScore.setText(CurrentPlayerModel.score);
        CurrentWin.setText(CurrentPlayerModel.wins);
        CurrentLoss.setText(CurrentPlayerModel.losses);


    }

    @Override
    public void start(Stage stage) throws Exception {



//        HamburgerBackArrowBasicTransition transition= new HamburgerBackArrowBasicTransition(Ham);
//        Ham.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
//            System.out.println("hamburger");
//            transition.setRate(transition.getRate()*-1);
//            transition.play();
//            if(Drawer.isOpened())
//                Drawer.close();
//            else
//                Drawer.open();
//        } );
//    }

    }
}