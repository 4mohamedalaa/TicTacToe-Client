package com.example.tictactoe.controllers;

import com.example.tictactoe.*;
import com.example.tictactoe.models.PlayerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOnlinePlayers();
    ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();
    ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOfflinePlayers();
    ObservableList<Object> OnPlayerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean isPressed = false;

        // Online table list
        OnlinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OnlinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        OnlinePlayerInvite.setCellValueFactory(new PropertyValueFactory<>("InviteBtn"));

        // Offline table list
        OfflinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OfflinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        OfflinePlayerLoss.setCellValueFactory(new PropertyValueFactory<>("losses"));

        // Initialize the view
        Thread th = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    synchronized (ClientServerListener.onlinePlayersList){
                        try {
                            ClientServerListener.onlinePlayersList.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (ClientServerListener.offlinePlayersList){
                        try {
                            ClientServerListener.offlinePlayersList.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<PlayerModel> OnPlayers = (ArrayList<PlayerModel>) ClientServerListener.onlinePlayersList;
                    ObservableList<Object> OnPlayerList = FXCollections.observableArrayList();
                    ArrayList<PlayerModel> OffPlayers = ClientServerListener.offlinePlayersList;
                    ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();

                    initializeView();
                }
            }
        };
        th.setDaemon(true);
        th.start();
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

    public void initializeView(){
        // Online players list updated from Server
        ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOnlinePlayers();
        ObservableList<PlayerModel> OnPlayerList = FXCollections.observableArrayList();

        // Offline players list updated from server
        ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOfflinePlayers();
        ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();

        List<Button> buttonInvite =new ArrayList<>();

        for(int i=0; i<OnPlayers.size(); i++) {
            String OnName = OnPlayers.get(i).getUsername();
            Integer OnWins = OnPlayers.get(i).getWins();

            PlayerModel OnPlayer = new PlayerModel(

            );
//            OnPlayer.setInviteBtn(buttonInvite.get(i));
            buttonInvite.add(OnPlayer.getInviteBtn());
            // Initialize player
            OnPlayer.setUsername(OnName);
            OnPlayer.setWins(OnWins);
            OnPlayer.setId(OnPlayers.get(i).getId());
            // Add initialized object to list
            OnPlayerList.add(OnPlayer);
        }
        OnlinePlayers.getItems().setAll(OnPlayerList);


        for (int b = 0; b <buttonInvite.size() ; b++) {
            System.out.println("hallo invite loop");
//            buttonInvite.get(b).setOnAction(this::handleInviteButton);
            OnPlayerList.get(b).setInviteButtonHandler();
        }

        for(int O=0; O<OffPlayers.size(); O++){
            String OffName= OffPlayers.get(O).getUsername();
            Integer OffScore=OffPlayers.get(O).getScore();
            Integer OffLosses=OffPlayers.get(O).getLosses();

            PlayerModel Offplayer = new PlayerModel();
            Offplayer.setUsername(OffName);
            Offplayer.setWins(OffScore);
            Offplayer.setLosses(OffLosses);
            OffPlayerList.add(Offplayer);
        }
        OfflinePlayers.getItems().setAll(OffPlayerList);
    }
}
