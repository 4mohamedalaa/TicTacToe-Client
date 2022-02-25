package com.example.tictactoe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TablePlayers implements Initializable {

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

//private Button inviteBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //online player list
        OnlinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OnlinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOnlinePlayers();
        ObservableList<PlayerModel> OnPlayerList = FXCollections.observableArrayList();

        for(int i=0; i<OnPlayers.size(); i++){
            String Name= OnPlayers.get(i).getUsername();
            Integer Wins=OnPlayers.get(i).getWins();

            PlayerModel OnPlayer = new PlayerModel();
            OnPlayer.setUsername(Name);
            OnPlayer.setWins(Wins);
            OnPlayerList.add(OnPlayer);
        }
        OnlinePlayers.getItems().setAll(OnPlayerList);



        //offline table list
        OfflinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OfflinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        OfflinePlayerLoss.setCellValueFactory(new PropertyValueFactory<>("losses"));


        ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOfflinePlayers();
        ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();

        for(int i=0; i<OffPlayers.size(); i++){
            String Name= OffPlayers.get(i).getUsername();
            Integer Score=OffPlayers.get(i).getScore();
            Integer Losses=OffPlayers.get(i).getLosses();

            PlayerModel Offplayer = new PlayerModel();
            Offplayer.setUsername(Name);
            Offplayer.setWins(Score);
            Offplayer.setLosses(Losses);
            OffPlayerList.add(Offplayer);
        }
        OfflinePlayers.getItems().setAll(OffPlayerList);
    }

}
