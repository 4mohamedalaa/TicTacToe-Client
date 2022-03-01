package com.example.tictactoe;

import com.example.tictactoe.controllers.MultiGameController;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PlayerModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

import static com.example.tictactoe.controllers.LoginController.myControllerHandle1;

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
    // Declaring buttons and controller
    public boolean running = true;
    private static ArrayList<javafx.scene.control.Button> buttons;
    public static MultiGameController guest ;
    public static MultiGameController host ;


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
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Switching scenes
                                            Stage stage;
                                            stage = CurrentPlayerModel.eventWindow;
                                            Scene scene;
                                            Parent root;
                                            FXMLLoader loader = new FXMLLoader(
                                                    getClass().getResource("/fxml/multiPlayer.fxml"));
                                            try {
                                                root = loader.load();
                                                guest = loader.getController();
                                                System.out.println(guest);
                                                //myControllerHandle2.player2Name.setText(CurrentPlayerModel.username);
                                                System.out.println("inside invitation received");//sambo sending to said
                                                System.out.println("host : " +CurrentPlayerModel.username);
                                                System.out.println("guest : "+CurrentPlayerModel.opponentUsername);
                                                scene = new Scene(root);
                                                stage.setScene(scene);
                                                System.out.println();
                                                stage.show();
                                                guest.player2Name.setText(CurrentPlayerModel.opponentUsername) ;
                                                guest.player2Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                                guest.player2Name.setStyle("-fx-background-color: green");
                                                guest.player1Name.setText(CurrentPlayerModel.username);
                                                guest.player1Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                                guest.player1Name.setStyle("-fx-background-color: green");
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
                        CurrentPlayerModel.opponentUsername = jsonObject.get("acceptername").getAsString() ;
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
                                   // myControllerHandle2.player1Name.setText(CurrentPlayerModel.username);
                                    System.out.println("inside invitation accept");
                                    System.out.println("host : " +CurrentPlayerModel.username);
                                    System.out.println("guest : "+CurrentPlayerModel.opponentUsername);
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    host.player2Name.setText(CurrentPlayerModel.username) ;
                                    host.player2Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                    host.player2Name.setStyle("-fx-background-color: green");
                                    host.player1Name.setText(CurrentPlayerModel.opponentUsername);
                                    host.player1Name.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 14));
                                    host.player1Name.setStyle("-fx-background-color: green");
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
                            synchronized (onlinePlayersList){
                                onlinePlayersList.notifyAll();
                            }
                        }

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
                            synchronized (offlinePlayersList){
                                offlinePlayersList.notifyAll();
                            }
                        }
                        System.out.println("received update-list");
                        break;

                    case"receivemessagefromone":
                        String senderUserName =  jsonObject.get("senderusername").getAsString();
                        String msg = jsonObject.get("message").getAsString();
                        String message1 = senderUserName.concat(" : ").concat(msg).concat("\n");
                        System.out.println("********  inside receive message from one  ");
                        if(host==null)   {
                            System.out.println("host is null");
                            guest.txtA.appendText(message1);
                        }
                        if(guest==null){
                            System.out.println("guest is null");
                            host.txtA.appendText(message1);
                        }
                        break;

                    case "oponnetmove" :
                        int position=jsonObject.get("position").getAsInt();
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
                        // System.out.println(jsonObject);
                        // String moves = jsonObject.get("moves").getAsString();
                        // Platform.runLater(new Runnable() {
                        // @Override
                        // public void run() {
                        // Stage stage;
                        // stage = CurrentPlayerModel.eventWindow;
                        // Scene scene;
                        // Parent root;
                        // FXMLLoader loader = new
                        // FXMLLoader(getClass().getResource("/fxml/multiPlayer.fxml"));
                        // try {
                        // root = loader.load();
                        // myControllerHandle2 = loader.getController();
                        // scene = new Scene(root);
                        // stage.setScene(scene);
                        // stage.show();
                        // } catch (IOException e) {
                        // e.printStackTrace();
                        // }
                        // }
                        // });
                        // String[] string = recordsArray.replaceAll("\[", "").replaceAll("]",
                        // "").split(",");
                        // for (int i = 0; i < string.length; i++) {
                        // String[] st = string[i].trim().split("-");
                        // System.out.println(Arrays.toString(st));
                        //
                        //
                        // int pos = Integer.parseInt(st[0]);
                        // int sign = Integer.parseInt(st[1]);
                        // int player_id = Integer.parseInt(st[2]);
                        //
                        //
                        // double tim = i + 0.5;
                        //
                        // PauseTransition pause = new PauseTransition(Duration.seconds(i));
                        // pause.setOnFinished(event -> {
                        // Button btn = buttons.get(pos);
                        // btn.setFont(new Font("System Bold Italic", 200));
                        // btn.setStyle("-fx-font-size:40");
                        // String si = (sign == 8) ? "X" : "O";
                        // btn.setText(si);
                        // });
                        // pause.playFromStart();
                        // }
                        break;
                    case "opponent_disconnect":
                        // ServerConnector.dataOutputStream.close();
                        // ServerConnector.dataInputStream.close();
                        // System.out.println("opponent_disconnect");
                        // ServerConnector.socket.close();
                        // running=false;
                        // Platform.runLater(new Runnable() {
                        // @Override
                        // public void run() {
                        // //render pop up
                        // Alert alert = new Alert(Alert.AlertType.WARNING);
                        // alert.setContentText("Connection failed");
                        // alert.setTitle("connection");
                        // alert.initOwner(primaryStage);
                        //
                        //
                        // alert.getButtonTypes();
                        //
                        // Optional<ButtonType> result = alert.showAndWait();
                        // if (result.get() == ButtonType.OK){
                        // // ... user chose OK button
                        // Home root = new Home(primaryStage);
                        // Scene scene = new Scene(root);
                        // primaryStage.setTitle("home screen ");
                        // primaryStage.setScene(scene);
                        // primaryStage.show();
                        //
                        // }
                        //
                        // }
                        // });
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
                e.printStackTrace();
            }
        }
    }
}
