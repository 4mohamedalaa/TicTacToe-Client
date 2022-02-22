package com.example.tictactoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientServerHandler {
    private static final String SERVER_ADDRESS = "3.70.169.200";
    private static final String SERVER_PORT = "5001";
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static Socket socket;

    public static void connectSocket(){
        if(socket == null || socket.isClosed()){
            try {
                socket = new Socket(SERVER_ADDRESS, Integer.parseInt(SERVER_PORT));
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean signUp(String userName, String password){
        // Declare variables with validated username & hashed password
        String validatedUserName = validateUserName(userName);
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
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            String response = dataInputStream.readUTF();
            JsonObject resPayload = JsonParser.parseString(response).getAsJsonObject();
            String serverResponse = resPayload.get("successful").getAsString();
            // Only if the server response is true, then sign-up is valid
            if(serverResponse.equals("true")){ validSignUp = true;}
        }catch (IOException e){
            e.printStackTrace();
        }
        return validSignUp;
    }

    public static String signIn(String userName, String password){
        String hashedPassword = hashPassword(password);
        connectSocket();
        // Build Json payload
        JsonObject jsonSignInPayload=new JsonObject();
        jsonSignInPayload.addProperty("type","login");
        jsonSignInPayload.addProperty("username", userName);
        jsonSignInPayload.addProperty("password", hashedPassword);
        try {
            dataOutputStream.writeUTF(jsonSignInPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String resMsg= dataInputStream.readUTF();
            JsonObject response = JsonParser.parseString(resMsg).getAsJsonObject();
            System.out.println(response);
            String type = response.get("type").getAsString();
            if(type.equals("loginresponse")){
                PlayerInfo.login=response.get("successful").getAsString();
                if(PlayerInfo.login.equals("true")) {
                    PlayerInfo.id = response.get("id").getAsString();
                    PlayerInfo.username = response.get("username").getAsString();
                    PlayerInfo.score = response.get("score").getAsString();
                    PlayerInfo.wins = response.get("wins").getAsString();
                    PlayerInfo.losses = response.get("losses").getAsString();
                }
            }else{}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PlayerInfo.login;
    }

    public static void signOut(){
        JsonObject signOutPayload = new JsonObject();
        signOutPayload.addProperty("type", "logout");
        signOutPayload.addProperty("username", PlayerInfo.username);
        try {
            dataOutputStream.writeUTF(signOutPayload.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String validateUserName(String input){
        // Regex to validate usernames -- standardized for no ._ combinations or at start or end of string
        String regex = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()){return input;} else return null;
    }
    private static String hashPassword(String input){
        try {
            // Call SHA-1 Algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // Digest information into bytes, return array of byte
            byte[] messageDigestByte = messageDigest.digest(input.getBytes());
            // Convert array into signum
            BigInteger num = new BigInteger(1, messageDigestByte);
            // Convert message digest into hex value
            String hashText = num.toString(16);
            // add preceding 0's to make to 32 bit
            while(hashText.length() < 32){
                hashText = "0" + hashText;
            }
            // Return hashed password
            return hashText;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
}
