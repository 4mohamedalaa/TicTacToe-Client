package com.example.tictactoe;
//import com.example.tictactoe.controllers.MultiGameController;
import com.example.tictactoe.controllers.MultiGameController;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PausedGame;
import com.example.tictactoe.models.PlayerModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.tictactoe.controllers.LoginController.clientServerListener;


public class ClientServerHandler {
    //private static final String SERVER_ADDRESS = "18.197.17.158";
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final String SERVER_PORT = "5001";
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    public static Socket socket = connectSocket();
    private static ArrayList<javafx.scene.control.Button> buttons;

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
        requestObject.addProperty("opponentscore", CurrentPlayerModel.opponentscore);
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
            System.out.println("server is down ");
            //e.printStackTrace();
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
            clientServerListener.dataInputStream.close();
            clientServerListener.dataOutputStream.close();
            clientServerListener.socket.close();
            clientServerListener.running = false ;
            //@sayed---------------------------------------
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            CurrentPlayerModel.resetCurrentPlayer(); // CurrentPlayerModel reset


            //---------------------------------------------
            //@sayed---------------------------------------
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            CurrentPlayerModel.resetCurrentPlayer(); // CurrentPlayerModel reset


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
    /* public static void sendMessageToOne(String msg, String username) {
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
    }*/
    public static void sendMessageToOne(String msg ,String username){
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("type","sendmessageforone");
        responseObject.addProperty("senderusername",username);
        responseObject.addProperty("recieverid", CurrentPlayerModel.opponentId);
        responseObject.addProperty("message",msg);
        System.out.println("********  recieverid  "+ CurrentPlayerModel.opponentId);
        System.out.println("********  sender name "+ username);
        System.out.println("********  message "+ msg);

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

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("type", "play");
        requestObject.addProperty("opponet", CurrentPlayerModel.opponentId);
        requestObject.addProperty("game_id", CurrentPlayerModel.gameId);
        requestObject.addProperty("position", position);
        requestObject.addProperty("sign", sign);
        System.out.println("position : " + position+" sign : " + sign);
        System.out.println("from inside play function :");
        System.out.println("game id : " + CurrentPlayerModel.gameId);
        System.out.println("opponent id : " + CurrentPlayerModel.opponentId);
        try {
            dataOutputStream.writeUTF(requestObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //opponentsMove();

    }
    public static void renderRecordedGame(String recordsArray) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage;
                stage = CurrentPlayerModel.eventWindow;
                Scene scene;
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/multiPlayer.fxml"));
                try {
                    root = loader.load();
                    MultiGameController host = loader.getController();
                    System.out.println(host);
                    // myControllerHandle2.player1Name.setText(CurrentPlayerModel.username);
                    System.out.println("inside invitation accept");
                    System.out.println("host : " + CurrentPlayerModel.username);
                    System.out.println("guest : " + CurrentPlayerModel.opponentUsername);
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });
        String[] string = recordsArray.replaceAll("\\[", "")
                .replaceAll("]", "")
                .split(",");
        for (int i = 0; i < string.length; i++) {
            String[] st = string[i].trim().split("-");
            System.out.println(Arrays.toString(st));
            int pos = Integer.parseInt(st[0]);
            int sign = Integer.parseInt(st[1]);
            int player_id = Integer.parseInt(st[2]);
            double tim = i + 0.5;
            PauseTransition pause = new PauseTransition(Duration.seconds(i));
            pause.setOnFinished(event -> {
                Button btn = buttons.get(pos);
                btn.setFont(new Font("System Bold Italic", 200));
                btn.setStyle("-fx-font-size:40");
                String si = (sign == 8) ? "X" : "O";
                btn.setText(si);
            });
            pause.playFromStart();
        }
    }

    public static void sendReplayreq(JsonObject showRecObj) {
        try {
            dataOutputStream.writeUTF(showRecObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendPause(JsonObject pause) {
        try {
            dataOutputStream.writeUTF(pause.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public static void sendpauseAccept() {
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("type", "acceptpause");
        requestObject.addProperty("status" , "true");
        requestObject.addProperty("game_id", CurrentPlayerModel.gameId);
        requestObject.addProperty("accepter", CurrentPlayerModel.id);
        requestObject.addProperty("accepted", CurrentPlayerModel.opponentId);
        System.out.println("Accepted this invitation: " + requestObject);
        try {
            dataOutputStream.writeUTF(requestObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void sendFinishingObj(JsonObject gameFinish) {
        try {

            dataOutputStream.writeUTF(gameFinish.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void sendPausingObj(JsonObject gameFinish) {
        try {

            dataOutputStream.writeUTF(gameFinish.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendExit(JsonObject exit) {
        try {
            dataOutputStream.writeUTF(exit.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void askForPausedGames(int x) {
       // ArrayList<PlayerModel> pGames = new ArrayList<PlayerModel>();
        connectSocket();
        JsonObject req = new JsonObject();
        req.addProperty("type", "askforpausedgames");
        req.addProperty("playerId", "int");
        try {
            dataOutputStream.writeUTF(req.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return ClientServerListener.pausedMatchesList;
    }
   /* public static void passMoveToOponnent(JsonObject boardUpdate) {
        try {
            dataOutputStream.writeUTF(String.valueOf(boardUpdate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
