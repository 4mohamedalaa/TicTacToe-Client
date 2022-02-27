package com.example.tictactoe;

import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PlayerModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tictactoe.controllers.LoginController.clientServerListener;

public class ClientServerHandler {
    private static final String SERVER_ADDRESS = "18.197.17.158";
    private static final String SERVER_PORT = "5001";
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    public static Socket socket = connectSocket();

    // Insure we're connected to the server's socket
    public static Socket connectSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(SERVER_ADDRESS, Integer.parseInt(SERVER_PORT));
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    public static void acceptInvitation() {
        connectSocket();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("type", "acceptinvetation");
        requestObject.addProperty("game_id", CurrentPlayerModel.gameId);
        requestObject.addProperty("accepter", CurrentPlayerModel.id);
        requestObject.addProperty("accepted", CurrentPlayerModel.opponentId);
        System.out.println("Accepted this invitation: " + requestObject);
        try {
            dataOutputStream.writeUTF(requestObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Takes the PlayerModel of the user you'd like to invite as input
    public static void sendInvitation(PlayerModel opponentPlayer) {
        connectSocket();
        JsonObject invitiationPayload = new JsonObject();
        invitiationPayload.addProperty("type", "sendInvitation");
        invitiationPayload.addProperty("senderplayerid", CurrentPlayerModel.id);
        invitiationPayload.addProperty("senderusername", CurrentPlayerModel.username);
        invitiationPayload.addProperty("senderscore", CurrentPlayerModel.score);
        invitiationPayload.addProperty("sendtoid", opponentPlayer.getId());
        CurrentPlayerModel.currentlyInvitedPlayerId = opponentPlayer.getId();
        try {
            dataOutputStream.writeUTF(invitiationPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Enabled over-loading, can also send the raw opponent ID
    public static void sendInvitation(int opponentPlayerId) {
        connectSocket();
        JsonObject invitiationPayload = new JsonObject();
        invitiationPayload.addProperty("type", "sendInvitation");
        invitiationPayload.addProperty("senderplayerid", CurrentPlayerModel.id);
        invitiationPayload.addProperty("senderusername", CurrentPlayerModel.username);
        invitiationPayload.addProperty("senderscore", CurrentPlayerModel.score);
        invitiationPayload.addProperty("sendtoid", opponentPlayerId);
        CurrentPlayerModel.currentlyInvitedPlayerId = opponentPlayerId;
        System.out.println("Sent invite: " + invitiationPayload);
        try {
            dataOutputStream.writeUTF(invitiationPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Called to get currently online players list
    public static ArrayList<PlayerModel> getOnlinePlayers() {
        ArrayList<PlayerModel> listOfPlayers = new ArrayList<PlayerModel>();
        connectSocket();
        JsonObject reqOffPlayers = new JsonObject();
        reqOffPlayers.addProperty("type", "onlineplayers");
        try {
            dataOutputStream.writeUTF(reqOffPlayers.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ClientServerListener.onlinePlayersList;
    }

    // Called to get currently offline players list
    public static ArrayList<PlayerModel> getOfflinePlayers() {
        ArrayList<PlayerModel> listOfPlayers = new ArrayList<PlayerModel>();
        connectSocket();
        JsonObject reqOffPlayers = new JsonObject();
        reqOffPlayers.addProperty("type", "offlineplayers");
        try {
            dataOutputStream.writeUTF(reqOffPlayers.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ClientServerListener.offlinePlayersList;
    }

    // Called when user wants to make a new account
    public static boolean signUp(String userName, String password) {
        // Declare variables with validated username & hashed password
        String validatedUserName = validateUserName(userName);
        if (validatedUserName == null) {
            return false;
        } // If username is invalid, return false to controller
        String hashedPassword = hashPassword(password);
        boolean validSignUp = false; // response for successful sign-up, defaulted to false
        connectSocket(); // Insure socket connection'
        System.out.println(socket);
        // Create Json payload
        JsonObject jsonSignUpPayload = new JsonObject();
        jsonSignUpPayload.addProperty("type", "signup");
        jsonSignUpPayload.addProperty("username", validatedUserName);
        jsonSignUpPayload.addProperty("password", hashedPassword);
        try {
            dataOutputStream.writeUTF(jsonSignUpPayload.toString()); // Send Json payload to server
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String response = dataInputStream.readUTF();
            JsonObject resPayload = JsonParser.parseString(response).getAsJsonObject();
            String serverResponse = resPayload.get("successful").getAsString();
            // Only if the server response is true, then sign-up is valid
            if (serverResponse.equals("true")) {
                validSignUp = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return validSignUp;
    }

    // Called to sign in a user into server
    public static boolean signIn(String userName, String password) {
        String hashedPassword = hashPassword(password);
        connectSocket();
        // Build Json payload
        JsonObject jsonSignInPayload = new JsonObject();
        jsonSignInPayload.addProperty("type", "login");
        jsonSignInPayload.addProperty("username", userName);
        jsonSignInPayload.addProperty("password", hashedPassword);
        try {
            dataOutputStream.writeUTF(jsonSignInPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String resMsg = dataInputStream.readUTF();
            JsonObject response = JsonParser.parseString(resMsg).getAsJsonObject();
            System.out.println(response);
            String type = response.get("type").getAsString();
            if (type.equals("loginresponse")) {
                CurrentPlayerModel.login = Boolean.valueOf(response.get("successful").getAsString());
                if (CurrentPlayerModel.login) {
                    CurrentPlayerModel.id = response.get("id").getAsString();
                    CurrentPlayerModel.username = response.get("username").getAsString();
                    CurrentPlayerModel.score = response.get("score").getAsString();
                    CurrentPlayerModel.wins = response.get("wins").getAsString();
                    CurrentPlayerModel.losses = response.get("losses").getAsString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CurrentPlayerModel.login;
    }

    // Called to sign a user out of the server
    public static void signOut() {
        JsonObject signOutPayload = new JsonObject();
        signOutPayload.addProperty("type", "logout");
        signOutPayload.addProperty("username", CurrentPlayerModel.username);
        try {
            dataOutputStream.writeUTF(signOutPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //@sambo------------------------------------
            clientServerListener.running = false ;
            clientServerListener.socket.close();
            clientServerListener.dataInputStream.close();
            clientServerListener.dataOutputStream.close();
            clientServerListener.stop();
            //---------------------------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String validateUserName(String input) {
        // Regex to validate usernames -- standardized for no ._ combinations or at
        // start or end of string
        String regex = "^(?=.{4,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return input;
        } else
            return null;
    }

    // Hash a user's password
    private static String hashPassword(String input) {
        try {
            // Call SHA-1 Algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // Digest information into bytes, return array of byte
            byte[] messageDigestByte = messageDigest.digest(input.getBytes());
            // Convert array into signum
            BigInteger num = new BigInteger(1, messageDigestByte);
            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(num.toString(16));
            // add preceding 0's to make to 32 bit
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            // Return hashed password
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // @Sambo
    // sending message in global chat logic
    public static void sendMessageForAll(String msg, String username) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("type", "sendmessageforall");
        responseObject.addProperty("username", username);
        responseObject.addProperty("message", msg);
        try {

            dataOutputStream.writeUTF(responseObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Sambo
    // sending message in global chat logic
    public static void sendMessageToOne(String msg, String username) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("type", "sendmessageforone");
        responseObject.addProperty("senderusername", username);
        // responseObject.addProperty("recieverid",
        // Integer.parseInt(CurentPlayerModel.opponentId));
        responseObject.addProperty("message", msg);
        try {
            dataOutputStream.writeUTF(responseObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(JsonObject closingObj) {

            try {
                dataOutputStream.writeUTF(closingObj.toString());
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
                clientServerListener.running = false ;
            } catch (IOException e) {

            }

    }

    public static void play(int position, int sign) {

            JsonObject requestObject=new JsonObject();
            requestObject.addProperty("type","play");
            requestObject.addProperty("opponet",CurrentPlayerModel.opponentId);
            requestObject.addProperty("game_id",CurrentPlayerModel.gameId);
            requestObject.addProperty("position",position);
            requestObject.addProperty("sign",sign);
            System.out.println(position);
            try {
                dataOutputStream.writeUTF(requestObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //opponentsMove();


    }

    public static void sendFinishingObj(JsonObject gameFinish) {
        try {
            dataOutputStream.writeUTF(gameFinish.toString());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public static void passMoveToOponnent(JsonObject boardUpdate) {
        try {
            dataOutputStream.writeUTF(String.valueOf(boardUpdate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // boolean running = true;
    // Thread thread;
    // public void ServerConnector(){
    // thread = new Thread(()->{
    // System.out.println("readeron");
    // System.out.println(running);
    // while (running)
    // {
    // try {
    // String lineSent = dataInputStream.readUTF();
    // if (lineSent == null) throw new IOException();
    // JsonObject requestObject =
    // JsonParser.parseString(lineSent).getAsJsonObject();
    // String type = requestObject.get("type").getAsString();
    // System.out.println(type);
    // switch (type) {
    // case "oponnetmove" :
    // int position=requestObject.get("position").getAsInt();
    // opponentsMove(position);
    //
    // break;
    // case "loginresponse" -> System.out.println("responsethroughthread");
    //
    // case "yourinvetationaccepted":
    // int accepterID=requestObject.get("whoaccepted").getAsInt();
    // CurrentPlayerModel.opponentId = String.valueOf(accepterID);
    // CurrentPlayerModel.gameId=requestObject.get("game_id").getAsInt();
    // CurrentPlayerModel.playerTurn=false;
    // CurrentPlayerModel.mySign="O";
    // Platform.runLater(new Runnable() {
    // @Override
    // public void run() {
    // boolean playAgainstPC=false;
    // playonlinescreen.getinvitationAlert().hide();
    // System.out.println("newgameboard");
    // GameBoard root = new GameBoard(primaryStage, playAgainstPC,false,false);
    // Scene scene = new Scene(root);
    // primaryStage.setTitle("GameBoard screen ");
    // primaryStage.setScene(scene);
    // primaryStage.show();
    // }
    // });
    // break;
    // case "invitationreceived" -> {
    // int opponentID = requestObject.get("sender").getAsInt();
    // CurrentPlayerModel.gameId = requestObject.get("game_id").getAsInt();
    // CurrentPlayerModel.opponentId = String.valueOf(opponentID);
    // CurrentPlayerModel.opponentUsername =
    // requestObject.get("opponentusername").getAsString();
    // CurrentPlayerModel.opponentScore =
    // requestObject.get("opponentsscore").getAsInt();
    // System.out.println(opponentID + "chalenges you");
    // Platform.runLater(new Runnable() {
    // @Override
    // public void run() {
    // Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "waiting for
    // response...", ButtonType.NO, ButtonType.YES);
    // alert2.setTitle("invitation");
    // alert2.setHeaderText("Do you want to play with " +
    // CurrentPlayerModel.opponentUsername + " ?");
    // alert2.setResizable(false);
    // alert2.initOwner(primaryStage);
    // Optional<ButtonType> result = alert2.showAndWait();
    // ButtonType button = result.orElse(ButtonType.NO);
    //
    // if (button == ButtonType.YES) {
    // // if condition yes && no : call isma3el methods
    // System.out.println("yes"); //accept play
    // CurrentPlayerModel.playerTurn = true;
    // CurrentPlayerModel.allowFire = true;
    // CurrentPlayerModel.mySign = "X";
    // acceptInvetation();
    // boolean playAgainstPC = false;
    // System.out.println("newgameboard");
    // GameBoard root = new GameBoard(primaryStage, playAgainstPC, false, false);
    // Scene scene = new Scene(root);
    // primaryStage.setTitle("GameBoard screen ");
    // primaryStage.setScene(scene);
    // primaryStage.show();
    //
    //
    // } else {
    // System.out.println("noo"); // reject play
    // }
    // }
    // });
    // }
    // case "game_record" -> {
    // System.out.println(requestObject);
    // String moves = requestObject.get("moves").getAsString();
    // renderRecordedGame(moves);
    // }
    // case "offlineplayers" -> {
    // if (offlinePlayersFromServer != null) offlinePlayersFromServer.clear();
    // JsonArray offlinePlayers = requestObject.getAsJsonArray("offlineplayers");
    // System.out.println(offlinePlayers);
    // for (JsonElement rplayerobject : offlinePlayers) {
    // JsonObject playerObject = rplayerobject.getAsJsonObject();
    // Player player = new Player();
    // player.id = playerObject.get("id").getAsInt();
    // // System.out.println(player.id);
    // player.username = playerObject.get("username").getAsString();
    // player.score = playerObject.get("score").getAsInt();
    // offlinePlayersFromServer.add(player);
    // }
    // for (Player player : offlinePlayersFromServer) {
    // System.out.println(player.username);
    // }
    // }
    // case "onlineplayers" -> {
    // JsonArray onlinePlayers = requestObject.getAsJsonArray("onlineplayers");
    // if (onlinePlayersFromServer != null) onlinePlayersFromServer.clear();
    // System.out.println(onlinePlayers);
    // for (JsonElement rplayerobject : onlinePlayers) {
    // JsonObject playerObject = rplayerobject.getAsJsonObject();
    // Player player = new Player();
    // player.id = playerObject.get("id").getAsInt();
    // player.username = playerObject.get("username").getAsString();
    // player.score = playerObject.get("score").getAsInt();
    // onlinePlayersFromServer.add(player);
    // }
    // }
    // case "opponent_disconnect" -> {
    // ServerConnector.dataOutputStream.close();
    // ServerConnector.dataInputStream.close();
    // System.out.println("opponent_disconnect");
    // ServerConnector.socket.close();
    // running = false;
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
    // if (result.get() == ButtonType.OK) {
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
    // }
    // case "update-list" -> {
    // System.out.println("clientclosed");
    // JsonArray newonlinePlayers = requestObject.getAsJsonArray("onlineplayers");
    // if (onlinePlayersFromServer != null) onlinePlayersFromServer.clear();
    // //System.out.println(newonlinePlayers);
    // for (JsonElement rplayerobject : newonlinePlayers) {
    // JsonObject playerObject = rplayerobject.getAsJsonObject();
    // Player player = new Player();
    // player.id = playerObject.get("id").getAsInt();
    // player.username = playerObject.get("username").getAsString();
    // player.score = playerObject.get("score").getAsInt();
    // onlinePlayersFromServer.add(player);
    // }
    // for (Player offplayer : onlinePlayersFromServer) {
    // System.out.println("new onlineplayers");
    // System.out.println(offplayer.getUsername());
    // }
    // if (offlinePlayersFromServer != null) offlinePlayersFromServer.clear();
    // JsonArray newofflinePlayers = requestObject.getAsJsonArray("offlineplayers");
    // //System.out.println(offlinePlayers);
    // for (JsonElement rplayerobject : newofflinePlayers) {
    // JsonObject playerObject = rplayerobject.getAsJsonObject();
    // Player player = new Player();
    // player.id = playerObject.get("id").getAsInt();
    // // System.out.println(player.id);
    // player.username = playerObject.get("username").getAsString();
    // player.score = playerObject.get("score").getAsInt();
    // offlinePlayersFromServer.add(player);
    // }
    // for (Player offplayer : offlinePlayersFromServer) {
    // System.out.println("new offlineplayers");
    // System.out.println(offplayer.getUsername());
    // }
    // Platform.runLater(new Runnable() {
    // @Override
    // public void run() {
    // if (playonlinescreen != null)
    // playonlinescreen.renderLists(onlinePlayersFromServer,
    // offlinePlayersFromServer);
    // }
    // });
    // }
    // }
    // }catch (IOException e){}
    // try {
    // sleep(1000);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // );
    // thread.start();
    // }
}
