package com.example.tictactoe;

import com.example.tictactoe.controllers.ProfileController;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PlayerModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

import static com.example.tictactoe.controllers.LoginController.myControllerHandle1;
//import static com.example.tictactoe.controllers.ProfileController.myControllerHandle2;

public class ClientServerListener extends Thread {
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static Socket socket;
    private static String currentMsg;
    private static Stage primaryStage;
    // Created ArrayLists to track offline and online players in Real-Time
    public static ArrayList<PlayerModel> onlinePlayersList = new ArrayList<PlayerModel>();
    public static ArrayList<PlayerModel> offlinePlayersList = new ArrayList<PlayerModel>();

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

    @Override
    public void run() {
        super.run();
        while (true) {
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
                                } else {
                                    System.out.println("Invitation rejected");
                                }
                            }
                        });
                        break;
                    case "yourinvetationaccepted":
                        int accepterId = jsonObject.get("whoaccepted").getAsInt();
                        CurrentPlayerModel.opponentId = Integer.valueOf(String.valueOf(accepterId));
                        CurrentPlayerModel.gameId = jsonObject.get("game_id").getAsInt();
                        CurrentPlayerModel.playerTurn = false;
                        CurrentPlayerModel.mySign = "O";
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                boolean playAgainstPC = false;
                                // System.out.println("Your invitation was accepted, joining game ID: " +
                                // CurrentPlayerModel.gameId.toString());
                                System.out.println("Your accepted invitation is: " + jsonObject);
                            }
                        });
                        break;
                    case "playermove":

                        break;
                    case "opponentmove":
                        break;
                    case "onlineplayers":

                        break;
                    case "offlineplayers":
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
                        }
                        System.out.println("received update-list");
                        // Could create a thread to keep lists updated here of both offline and online
                        // players
                        break;

                    // @samboooo
                    // recieved Json from server to print message from one client to only another
                    // opponent one
                    // need controller of game board to be finished
//                    case "receivemessagefromone":
//                        String senderUserName = jsonObject.get("senderusername").toString();
//                        String msg = jsonObject.get("message").toString();
//                        String message1 = senderUserName.concat(" : ").concat(msg);
//                        myControllerHandle2.txtA.appendText(message1);
//                        myControllerHandle2.txtA.appendText("\n");
//                        break;

                    // @samboooo
                    // recieved Json from server to print message from one client to all online
                    // players
                    case "allreceivemessagefromone":
                        String name = jsonObject.get("senderusername").toString();
                        String message2 = jsonObject.get("message").toString();
                        String msgtoProfile = name.concat(" : ").concat(message2).concat("\n");
                        // System.out.println("********************");
                        // System.out.println(jsonObject);
                        // System.out.println("********************");
//                        myControllerHandle1.txtA.appendText(msgtoProfile);

                        break;
                    default:
                        System.out.println("Invalid server request");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
