package com.example.tictactoe.controllers;

import com.example.tictactoe.ClientServerHandler;
import com.example.tictactoe.models.PlayerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class TablePlayers implements Initializable  {

    @FXML
    TableView<PlayerModel> OnlinePlayers;

    @FXML
    TableColumn<PlayerModel, String> OnlinePlayerName;

    @FXML
    TableColumn <PlayerModel, Integer> OnlinePlayerScore;

    @FXML

    TableView<PlayerModel> OfflinePlayers;

    @FXML
    TableColumn<PlayerModel, String> OfflinePlayerName;

    @FXML
    TableColumn <PlayerModel, Integer> OfflinePlayerScore;

    @FXML
    TableColumn <PlayerModel, Integer> OfflinePlayerLoss;

    @FXML
    TableColumn <PlayerModel, Button>  OnlinePlayerInvite;

@FXML
Button BackBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //online player list
        OnlinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OnlinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
OnlinePlayerInvite.setCellValueFactory(new PropertyValueFactory<>("InviteBtn"));

//        ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOnlinePlayers();
        ObservableList<PlayerModel> OnPlayerList = FXCollections.observableArrayList();
//        Button []buttonInvite = new Button[OnPlayerList.size()];
        List<Button> buttonInvite =new ArrayList<>();

        //invite button



        for(int i=0; i<5; i++) {
//            String OnName = OnPlayers.get(i).getUsername();
//            Integer OnWins = OnPlayers.get(i).getWins();

            PlayerModel OnPlayer = new PlayerModel();
            buttonInvite.add(OnPlayer.getInviteBtn());
            //            OnPlayer.setUsername(OnName);
//            OnPlayer.setWins(OnWins);
//
//            System.out.println(OnName);
//            OnPlayer.setInviteBtn(buttonInvite[i]);

            OnPlayerList.add(OnPlayer);

        }
        System.out.println(buttonInvite.size());
        OnlinePlayers.getItems().setAll(OnPlayerList);

        for (int b = 0; b <buttonInvite.size() ; b++) {
            System.out.println("hallo invite loop");
//            buttonInvite.get(b) = new Button();
            buttonInvite.get(b).setOnAction(this::handleInviteButton);
        }


    //offline table list
//            OfflinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
//            OfflinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
//            OfflinePlayerLoss.setCellValueFactory(new PropertyValueFactory<>("losses"));
//
////
//            ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOfflinePlayers();
//            ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();
//
//            for(int O=0; O<OffPlayers.size(); O++){
//                String OffName= OffPlayers.get(O).getUsername();
//                Integer OffScore=OffPlayers.get(O).getScore();
//                Integer OffLosses=OffPlayers.get(O).getLosses();
//
//                PlayerModel Offplayer = new PlayerModel();
//                Offplayer.setUsername(OffName);
//                Offplayer.setWins(OffScore);
//                Offplayer.setLosses(OffLosses);
//                OffPlayerList.add(Offplayer);
//            }
//            OfflinePlayers.getItems().setAll(OffPlayerList);
//
//



  }
    public void BackBtn(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void handleInviteButton(ActionEvent inviteAction){
        System.out.println("hallo");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Send invitation");
//        alert.setGraphic();
        alert.showAndWait();
    }

}
