package com.example.tictactoe.controllers;

import com.example.tictactoe.*;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PausedGame;
import com.example.tictactoe.models.PlayerModel;
import javafx.application.Platform;
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


public class TablePlayers implements Initializable {
    @FXML
    TableView<PausedGame> PausedMatches;
    @FXML
    TableColumn<PausedGame, String> opponent;
    @FXML
    TableColumn<PausedGame, Button> resume;

    @FXML
    TableView<PlayerModel> OnlinePlayers;

    @FXML
    TableColumn<PlayerModel, String> OnlinePlayerName;

    @FXML
    TableColumn<PlayerModel, Integer> OnlinePlayerScore;

    @FXML

    TableView<PlayerModel> OfflinePlayers;

    @FXML
    TableColumn<PlayerModel, String> OfflinePlayerName;

    @FXML
    TableColumn<PlayerModel, Integer> OfflinePlayerScore;

    @FXML
    TableColumn<PlayerModel, Integer> OfflinePlayerLoss;

    @FXML
    TableColumn<PlayerModel, Button> OnlinePlayerInvite;

    @FXML
    Button BackBtn;

    /*ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOnlinePlayers();
    ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();

    ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOfflinePlayers();
    ObservableList<Object> OnPlayerList = FXCollections.observableArrayList();
*/
//    ArrayList<PausedGame> pgames = ClientServerListener.pausedMatchesList;
//    ObservableList<PausedGame> pgamesList = FXCollections.observableArrayList();

    // Provide status for running thread
    boolean isPressed = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        pgamesList.addAll(ClientServerListener.pausedMatchesList);
//        pgamesList.addAll(pgames);
        //Online table list
        OnlinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OnlinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        OnlinePlayerInvite.setCellValueFactory(new PropertyValueFactory<>("InviteBtn"));

        // Offline table list
        OfflinePlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
        OfflinePlayerScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        OfflinePlayerLoss.setCellValueFactory(new PropertyValueFactory<>("losses"));

        //paused matches
        opponent.setCellValueFactory(new PropertyValueFactory<>("opponentName"));
        resume.setCellValueFactory(new PropertyValueFactory<>("InviteBtn"));

        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  int current = Integer.parseInt(CurrentPlayerModel.id);
                                  ClientServerHandler.askForPausedGames(current);
//
//                                  ArrayList<PausedGame> pgames = ClientServerListener.pausedMatchesList;
//                                  ObservableList<PausedGame> ObservableGames = FXCollections.observableArrayList();
                                 /* List<Button> buttonInvite =new ArrayList<>();
                                  for(int i=0; i<pgames.size(); i++) {
                                      String opponent = pgames.get(i).getOpponent();
                                      Integer opponentID = pgames.get(i).getOpponentId();
                                      Integer gameID = pgames.get(i).getGame_id();
                                      PausedGame pgame = new PausedGame();
                                      buttonInvite.add(pgame.getInviteBtn());
                                      // Initialize player
                                      pgame.setOpponent(opponent);
                                      pgame.setOpponentId(opponentID);
                                      pgame.setGame_id(gameID);
                                      // Add initialized object to list
                                      ObservableGames.add(pgame);
                                  }
                                  PausedMatches.getItems().setAll(ObservableGames);
                                  for (int b = 0; b <buttonInvite.size() ; b++) {
                                      ObservableGames.get(b).setInviteButtonHandler();
                                  }*/

                              }
                          }
        );


        // Initialize the view
        Thread th = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isPressed) {
                    synchronized (ClientServerListener.onlinePlayersList) {
                        try {
                            // Raised wait higher for online players to not affect invite button GUI
                            ClientServerListener.onlinePlayersList.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (ClientServerListener.offlinePlayersList) {
                        try {
                            ClientServerListener.offlinePlayersList.wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (ClientServerListener.pausedMatchesList) {
                        try {
                            ClientServerListener.pausedMatchesList.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<PlayerModel> OnPlayers = (ArrayList<PlayerModel>) ClientServerListener.onlinePlayersList;
                    OnPlayers.removeIf(playerModel -> (playerModel.getId() == Integer.parseInt(CurrentPlayerModel.id)));
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
        isPressed = true;
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initializeView() {
        // Online players list updated from Server
        ArrayList<PlayerModel> OnPlayers = ClientServerHandler.getOnlinePlayers();
        ObservableList<PlayerModel> OnPlayerList = FXCollections.observableArrayList();

        // Offline players list updated from server
        ArrayList<PlayerModel> OffPlayers = ClientServerHandler.getOfflinePlayers();
        ObservableList<PlayerModel> OffPlayerList = FXCollections.observableArrayList();

        // paused matches list updated from server

        ArrayList<PausedGame> pausedGames = ClientServerListener.pausedMatchesList;
        ObservableList<PausedGame> pausedGamesList = FXCollections.observableArrayList();

        List<Button> buttonInvite = new ArrayList<>();
        for (int i = 0; i < OnPlayers.size(); i++) {
            String OnName = OnPlayers.get(i).getUsername();
            Integer OnWins = OnPlayers.get(i).getWins();

            PlayerModel OnPlayer = new PlayerModel();
            //OnPlayer.setInviteBtn(buttonInvite.get(i));
            buttonInvite.add(OnPlayer.getInviteBtn());
            // Initialize player
            OnPlayer.setUsername(OnName);
            OnPlayer.setWins(OnWins);
            OnPlayer.setId(OnPlayers.get(i).getId());
            OnPlayer.setScore(OnPlayers.get(i).getScore());
            OnPlayer.setLosses(OnPlayers.get(i).getLosses());
            // Add initialized object to list
            OnPlayerList.add(OnPlayer);
        }
        OnlinePlayers.getItems().setAll(OnPlayerList);
        for (int b = 0; b < buttonInvite.size(); b++) {
//            buttonInvite.get(b).setOnAction(this::handleInviteButton);
            OnPlayerList.get(b).setInviteButtonHandler();
        }


        for (int O = 0; O < OffPlayers.size(); O++) {
            String OffName = OffPlayers.get(O).getUsername();
            int OffScore = OffPlayers.get(O).getScore();
            int OffLosses = OffPlayers.get(O).getLosses();
            int OffWins = OffPlayers.get(O).getWins();

            PlayerModel Offplayer = new PlayerModel();
            Offplayer.setUsername(OffName);
            Offplayer.setWins(OffWins);
            Offplayer.setLosses(OffLosses);
            Offplayer.setScore(OffScore);
            OffPlayerList.add(Offplayer);
        }
        OfflinePlayers.getItems().setAll(OffPlayerList);

        //Add Resume elements to paused
        List<Button> ResumeBtns = new ArrayList<>();
        for (int i = 0; i < pausedGames.size(); i++) {
            PausedGame OnPausePlayer = new PausedGame();
//            System.out.println(pausedGames.get(i).getOpponentName());
            OnPausePlayer.setOpponentName(pausedGames.get(i).getOpponentName());
            OnPausePlayer.setGame_id(pausedGames.get(i).getGame_id());
            OnPausePlayer.setOpponentId(pausedGames.get(i).getOpponentId());
            OnPausePlayer.setOpponentName(pausedGames.get(i).getOpponentName());
            ResumeBtns.add(pausedGames.get(i).getInviteBtn());
            // Add initialized object to list
            pausedGamesList.add(OnPausePlayer);
            //System.out.println("************************");
            //System.out.println(pausedGames.get(i).getGame_id());
            //System.out.println(pausedGames.get(i).getOpponentId());

        }
        PausedMatches.getItems().setAll(pausedGamesList);
        for (int b = 0; b < ResumeBtns.size(); b++) {
            //buttonInvite.get(b).setOnAction(this::handleInviteButton);
            // pgamesList.get(b).setInviteButtonHandler();
            pausedGamesList.get(b).initializeResumeBtn();
        }



    }
}
