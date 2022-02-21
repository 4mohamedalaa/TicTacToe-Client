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

public class ClientServerHandler {
    private static final String SERVER_ADDRESS = "3.70.169.200";
    private static final String SERVER_PORT = "5001";
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static Socket socket;

    public static String signIn(String userName, String password){
        String hashedPassword = hashPassword(password);
        if(socket == null || socket.isClosed()){
            try {
                socket = new Socket(SERVER_ADDRESS, Integer.parseInt(SERVER_PORT));
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Build Json payload
        JsonObject jsonSignIn=new JsonObject();
        jsonSignIn.addProperty("type","login");
        jsonSignIn.addProperty("username",userName);
        jsonSignIn.addProperty("password", hashedPassword);
        try {
            dataOutputStream.writeUTF(jsonSignIn.toString());
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
