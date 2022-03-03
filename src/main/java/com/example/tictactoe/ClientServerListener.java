package com.example.tictactoe;

import com.example.tictactoe.controllers.MultiGameController;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PausedGame;
import com.example.tictactoe.models.PlayerModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Objects;
import java.util.Optional;

import static com.example.tictactoe.controllers.LoginController.myControllerHandle1;
//import static com.example.tictactoe.controllers.TablePlayers.Update;

public class ClientServerListener extends Thread {
    // Inherit dataStreams and socket from ClientServerHandler
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    public Socket socket;

    // Create a stage for dialog box
    private static Stage primaryStage;

    // Created ArrayLists to track offline and online players in Real-Time
    public static final ArrayList<PlayerModel> onlinePlayersList = new ArrayList<PlayerModel>();
    public static final ArrayList<PlayerModel> offlinePlayersList = new ArrayList<PlayerModel>();
    public static final ArrayList<PausedGame> pausedMatchesList = new ArrayList<PausedGame>();

    // Declaring buttons and controller
    public boolean running = true;
    private static ArrayList<javafx.scene.control.Button> buttons;
    public static MultiGameController guest;
    public static MultiGameController host;
    private Object multicontrollerhandler;


    public ClientServerListener() {
        setDaemon(true);
        start();
    }

    @Override
    public void start() {
        super.start();
        socket = ClientServerHandler.socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToMultiplayerMatch(ActionEvent event) throws IOException {

    }

    @Override
    public void run() {
        super.run();
        while (running) {
            try {
                String serverMsg = dataInputStream.readUTF();
                if (serverMsg == null)
                    throw new IOException();
                JsonObject jsonObject = JsonParser.parseString(serverMsg).getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                System.out.println(type);
                switch (type) {
                    case "invitationreceived":
                        CurrentPlayerModel.opponentId = jsonObject.get("sender").getAsInt();
                        CurrentPlayerModel.gameId = jsonObject.get("game_id").getAsInt();
                        CurrentPlayerModel.opponentscore = jsonObject.get("opponentsscore").getAsString();
                        CurrentPlayerModel.opponentUsername = jsonObject.get("opponentusername").getAsString();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert invitationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                                        "You received an invitation from User ID: " +
                                                CurrentPlayerModel.opponentId + " from user: " +
                                                CurrentPlayerModel.opponentUsername,
                                        ButtonType.NO, ButtonType.YES);
                                invitationAlert.setTitle(CurrentPlayerModel.opponentUsername + " invited you");
                                invitationAlert.setHeaderText("Do you want to accept?");
                                invitationAlert.setResizable(false);
                                invitationAlert.initOwner(primaryStage);
                                Optional<ButtonType> userAnswer = invitationAlert.showAndWait();
                                ButtonType button = userAnswer.orElse(ButtonType.NO);
                                if (button == ButtonType.YES) {
                                    System.out.println("Accepted invitation");
                                    CurrentPlayerModel.playerTurn = true;
                                    CurrentPlayerModel.allowFire = true;
                                    CurrentPlayerModel.mySign = "X";
                                    ClientServerHandler.acceptInvitation();
                                    //////////////////////////////////////////
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Switching scenes
                                            Stage stage;
                                            stage = CurrentPlayerModel.eventWindow;
                                            Scene scene;
                                            Parent root;
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/multiPlayer.fxml"));
                                            try {
                                                root = loader.load();
                                                guest = loader.getController();
                                                System.out.println(guest);
                                                System.out.println("inside invitation received");//sambo sending to said
                                                System.out.println("host : " + CurrentPlayerModel.username);
                                                System.out.println("guest : " + CurrentPlayerModel.opponentUsername);
                                                scene = new Scene(root);
                                                stage.setScene(scene);
                                                System.out.println();
                                                stage.show();
                                                guest.player2Name.setText(CurrentPlayerModel.opponentUsername);
                                                guest.player2Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                                guest.player2Name.setStyle("-fx-background-color: green");
                                                guest.player1Name.setText(CurrentPlayerModel.username);
                                                guest.player1Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                                guest.player1Name.setStyle("-fx-background-color: green");
                                                //guest.scoreX.setText(CurrentPlayerModel.score);
                                                //guest.scoreO.setText(CurrentPlayerModel.opponentscore);
                                                System.out.println("opoooooooonnnnent score " + CurrentPlayerModel.opponentscore);
                                                System.out.println(" score " + CurrentPlayerModel.score);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else {
                                    System.out.println("Invitation rejected");
                                }
                            }
                        });
                        break;

                    case "yourinvetationaccepted": // "acceptinvetation"
                        int accepterId = jsonObject.get("whoaccepted").getAsInt();
                        CurrentPlayerModel.opponentId = Integer.valueOf(String.valueOf(accepterId));
                        CurrentPlayerModel.gameId = jsonObject.get("game_id").getAsInt();
                        CurrentPlayerModel.playerTurn = false;
                        CurrentPlayerModel.mySign = "O";
                        CurrentPlayerModel.username = jsonObject.get("accptedname").getAsString();
                        CurrentPlayerModel.opponentUsername = jsonObject.get("acceptername").getAsString();
                        // CurrentPlayerModel.opponentscore = jsonObject.get("opponentscore").getAsString() ;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // Switching scenes
                                Stage stage;
                                stage = CurrentPlayerModel.eventWindow;
                                Scene scene;
                                Parent root;
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/multiPlayer.fxml"));
                                try {
                                    root = loader.load();
                                    host = loader.getController();
                                    System.out.println(host);
                                    System.out.println("inside invitation accept");
                                    System.out.println("host : " + CurrentPlayerModel.username);
                                    System.out.println("guest : " + CurrentPlayerModel.opponentUsername);
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    host.player2Name.setText(CurrentPlayerModel.username);
                                    host.player2Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                    host.player2Name.setStyle("-fx-background-color: green");
                                    host.player1Name.setText(CurrentPlayerModel.opponentUsername);
                                    host.player1Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                    host.player1Name.setStyle("-fx-background-color: green");
                                    //host.scoreO.setText(CurrentPlayerModel.score);
                                    System.out.println("opoooooooonnnnent score " + CurrentPlayerModel.opponentscore);
                                    System.out.println(" score " + CurrentPlayerModel.score);
                                    // host.scoreX.setText(CurrentPlayerModel.opponentscore);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case "update-list":
                        if (onlinePlayersList != null) {
                            onlinePlayersList.clear();
                        }
                        for (JsonElement responsePlayerObject : jsonObject.getAsJsonArray("onlineplayers")) {
                            JsonObject offlinePlayerObject = responsePlayerObject.getAsJsonObject();
                            // Instantiate a PlayerModel for each object in response
                            PlayerModel newlyOnlinePlayer = new PlayerModel(
                                    offlinePlayerObject.get("id").getAsInt(),
                                    offlinePlayerObject.get("username").getAsString(),
                                    offlinePlayerObject.get("score").getAsInt());
                            onlinePlayersList.add(newlyOnlinePlayer);
                            synchronized (onlinePlayersList) {
                                onlinePlayersList.notifyAll();
                            }
                        }
                        // Update(onlinePlayersList) ;

                        if (offlinePlayersList != null) {
                            offlinePlayersList.clear();
                        }
                        for (JsonElement responsePlayerObject : jsonObject.get("offlineplayers").getAsJsonArray()) {
                            JsonObject offlinePlayerObject = responsePlayerObject.getAsJsonObject();
                            // System.out.println(jsonObject);
                            // Create a player model, add details from JsonObject into newly created Player
                            // object
                            PlayerModel newlyOfflinePlayer = new PlayerModel(
                                    offlinePlayerObject.get("id").getAsInt(),
                                    offlinePlayerObject.get("username").getAsString(),
                                    offlinePlayerObject.get("score").getAsInt());
                            offlinePlayersList.add(newlyOfflinePlayer);
                            synchronized (offlinePlayersList) {
                                offlinePlayersList.notifyAll();
                            }
                        }
                        // Update(offlinePlayersList);
                        System.out.println("received update-list");
                        break;

                    case "receivemessagefromone":
                        String senderUserName = jsonObject.get("senderusername").getAsString();
                        String msg = jsonObject.get("message").getAsString();
                        String message1 = senderUserName.concat(" : ").concat(msg).concat("\n");
                        System.out.println("********  inside receive message from one  ");
                        if (host == null) {
                            System.out.println("host is null");
                            guest.txtA.appendText(message1);
                        }
                        if (guest == null) {
                            System.out.println("guest is null");
                            host.txtA.appendText(message1);
                        }
                        break;
                    // @samboooo
                    // recieved Json from server to print message from one client to all online
                    // players
                  /*  case "oponnetmove" :
                        multicontrollerhandler.opponent_action(jsonObject);
                        break;*/
                    case "oponnetmove":
                        int position = jsonObject.get("position").getAsInt();
                        MultiGameController.opponentsMove(position);
                        break;
                    case "allreceivemessagefromone":
                        String name = jsonObject.get("senderusername").getAsString();
                        String message2 = jsonObject.get("message").getAsString();
                        String msgtoProfile = name.concat(" : ").concat(message2).concat("\n");
                        // System.out.println("********************");
                        // System.out.println(jsonObject);
                        // System.out.println("********************");
                        myControllerHandle1.txtA.appendText(msgtoProfile + "\n");
                        break;

                    case "game_record":
                        System.out.println(jsonObject);
                        String moves = jsonObject.get("moves").getAsString();
                        ClientServerHandler.renderRecordedGame(moves);
                        break;
                    case "opponent_disconnect":
                        dataOutputStream.close();
                        dataInputStream.close();
                        System.out.println("opponent_disconnect");
                        socket.close();
                        running = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //render pop up
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Connection failed");
                                alert.setTitle("connection");
                                alert.initOwner(primaryStage);
                                alert.getButtonTypes();
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    Stage stage = CurrentPlayerModel.eventWindow;
                                    Scene scene;
                                    Parent root;
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
                                    try {
                                        root = loader.load();
                                        scene = new Scene(root);
                                        stage.setScene(scene);
                                        System.out.println();
                                        stage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                        break;
                    case "recievepasuerequest":
                        if (pausedMatchesList != null) {
                            pausedMatchesList.clear();
                        }
                        for (JsonElement game : jsonObject.get("replyforpausedgames").getAsJsonArray()) {
                            JsonObject pgame = game.getAsJsonObject();
                            // System.out.println(jsonObject);
                            // Create a player model, add details from JsonObject into newly created Player
                            // object
                            PausedGame newlyOfflinePlayer = new PausedGame(
                                    pgame.get("gameid").getAsInt(),
                                    pgame.get("opponentname").getAsString(),
                                    pgame.get("opponentId").getAsInt());
                            pausedMatchesList.add(newlyOfflinePlayer);
                            System.out.println(pgame);
                        }
                        break;
                    case "askingforPausing":
                        CurrentPlayerModel.opponentId = jsonObject.get("senderid").getAsInt();
                        CurrentPlayerModel.gameId = jsonObject.get("gameid").getAsInt();
                        CurrentPlayerModel.opponentUsername = jsonObject.get("sendername").getAsString();
                        System.out.println("*********************************************************");
                        System.out.println("i am admin2 : ");
                        System.out.println("*********************************************************");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert invitationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                                        "pausing request from : " +
                                                CurrentPlayerModel.opponentUsername,
                                        ButtonType.NO, ButtonType.YES);
                                invitationAlert.setTitle(CurrentPlayerModel.opponentUsername + " Puase !");
                                invitationAlert.setHeaderText("Do you want to accept?");
                                invitationAlert.setResizable(false);
                                invitationAlert.initOwner(primaryStage);
                                Optional<ButtonType> userAnswer = invitationAlert.showAndWait();
                                ButtonType button = userAnswer.orElse(ButtonType.NO);
                                if (button == ButtonType.YES) {
                                    //will finish game here
                                    //goes to profile
                                    JsonObject gamePause = new JsonObject();
                                    gamePause.addProperty("type", "pause-game");
                                    gamePause.addProperty("game_id", CurrentPlayerModel.gameId);
                                    ClientServerHandler.sendPausingObj(gamePause);
                                    System.out.println("okay Accepted Puase");
                                    //////////////////////////////////////////
                                    JsonObject requestObject = new JsonObject();
                                    requestObject.addProperty("type", "acceptpause");
                                    //requestObject.addProperty("status" , true);
                                    requestObject.addProperty("game_id", CurrentPlayerModel.gameId);
                                    requestObject.addProperty("accepter", Integer.parseInt(CurrentPlayerModel.id));
                                    requestObject.addProperty("accepted", CurrentPlayerModel.opponentId);
                                    System.out.println("Accepted this pause : " + requestObject);
                                    try {
                                        dataOutputStream.writeUTF(requestObject.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //////////////////////////////////////////
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Switching scenes
                                            Stage stage;
                                            stage = CurrentPlayerModel.eventWindow;
                                            Scene scene;
                                            Parent root;
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
                                            try {
                                                root = loader.load();
                                                scene = new Scene(root);
                                                stage.setScene(scene);
                                                stage.show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else {
                                    JsonObject gameFinish = new JsonObject();
                                    gameFinish.addProperty("type", "finish_game");
                                    gameFinish.addProperty("winner", CurrentPlayerModel.id);
                                    gameFinish.addProperty("looser", CurrentPlayerModel.opponentId);
                                    gameFinish.addProperty("game_id", CurrentPlayerModel.gameId);
                                    System.out.println("game is finished " + true);
                                    ClientServerHandler.sendFinishingObj(gameFinish);

                                    System.out.println("No Rejected Puase ");
                                    //////////////////////////////////////////
                                    JsonObject requestObject = new JsonObject();
                                    requestObject.addProperty("type", "rejectpause");
                                    // requestObject.addProperty("status" , false);
                                    requestObject.addProperty("game_id", CurrentPlayerModel.gameId);
                                    requestObject.addProperty("accepter", Integer.parseInt(CurrentPlayerModel.id));
                                    requestObject.addProperty("accepted", CurrentPlayerModel.opponentId);
                                    System.out.println("Rejected this pause : " + requestObject);
                                    try {
                                        dataOutputStream.writeUTF(requestObject.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //show winner dialoge
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            StackPane secondaryLayout2 = new StackPane();
                                            MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("/fxml/Winner.mp4").toExternalForm()));
                                            MediaView mediaView2 = new MediaView(videoForWinner);
                                            secondaryLayout2.getChildren().addAll(mediaView2);
                                            Scene secondScene2 = new Scene(secondaryLayout2, 420, 300);
                                            Stage secondStage2 = new Stage();
                                            secondStage2.setResizable(false);
                                            secondStage2.setScene(secondScene2);
                                            secondStage2.show();
                                            videoForWinner.play();
                                            PauseTransition delay = new PauseTransition(Duration.seconds(5));
                                            delay.setOnFinished(event -> secondStage2.close());
                                            delay.play();
                                        }
                                    });
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Switching scenes
                                            Stage stage;
                                            stage = CurrentPlayerModel.eventWindow;
                                            Scene scene;
                                            Parent root;
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
                                            try {
                                                root = loader.load();
                                                scene = new Scene(root);
                                                stage.setScene(scene);
                                                stage.show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    //////////////////////
                                    //////////////////////////////////////////
                                    System.out.println("reject ya loser ");
                                }
                            }
                        });
                        break;

                    case "pauseAcceptanceState":
                        System.out.println("inside pauseAcceptanceState ");
                        //String state = jsonObject.get("state").toString();
                        //System.out.println("**** "+state);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // Switching scenes
                                Stage stage;
                                stage = CurrentPlayerModel.eventWindow;
                                Scene scene;
                                Parent root;
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
                                try {
                                    root = loader.load();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        //finish game with no winner
                        //finish game with state false
                        break;
                    case "pauseRejectState":
                        System.out.println("inside no pause");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                StackPane secondaryLayout2 = new StackPane();
                                MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("/fxml/loser.mp4").toExternalForm()));
                                MediaView mediaView2 = new MediaView(videoForWinner);
                                secondaryLayout2.getChildren().addAll(mediaView2);
                                Scene secondScene2 = new Scene(secondaryLayout2, 420, 400);
                                Stage secondStage2 = new Stage();
                                secondStage2.setResizable(false);
                                secondStage2.setScene(secondScene2);
                                secondStage2.show();
                                videoForWinner.play();
                                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                                delay.setOnFinished(event -> secondStage2.close());
                                delay.play();
                            }
                        });
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // Switching scenes
                                Stage stage;
                                stage = CurrentPlayerModel.eventWindow;
                                Scene scene;
                                Parent root;
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
                                try {
                                    root = loader.load();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case "opponentwithdraw":
                        String player = jsonObject.get("sendername").toString();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert invitationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                                        player + " has been out , You Won the game  : " +
                                                ButtonType.OK);
                                invitationAlert.setTitle(CurrentPlayerModel.opponentUsername + " Winner !");
                                invitationAlert.setHeaderText("Do you want to accept?");
                                invitationAlert.setResizable(false);
                                invitationAlert.initOwner(primaryStage);
                                Optional<ButtonType> userAnswer = invitationAlert.showAndWait();
                            }
                        });
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                StackPane secondaryLayout2 = new StackPane();
                                MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("/fxml/Winner.mp4").toExternalForm()));
                                MediaView mediaView2 = new MediaView(videoForWinner);
                                secondaryLayout2.getChildren().addAll(mediaView2);
                                Scene secondScene2 = new Scene(secondaryLayout2, 420, 300);
                                Stage secondStage2 = new Stage();
                                secondStage2.setResizable(false);
                                secondStage2.setScene(secondScene2);
                                secondStage2.show();
                                videoForWinner.play();
                                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                                delay.setOnFinished(event -> secondStage2.close());
                                delay.play();
                            }
                        });
                        break;

                    default:
                        System.out.println("Invalid server request");
                }
            } catch (IOException e) {
                System.out.println("Client Signed Out");
                // e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("client closed");
            }
        }
    }
}
