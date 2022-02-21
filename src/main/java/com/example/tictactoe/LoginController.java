package com.example.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class LoginController {
    @FXML
    Button loginBtn;
    @FXML
    TextField userTxt;
    @FXML
    PasswordField passwordField;

    public void loginBtnAction(){
        System.out.println("Pressed action button");
        // If the fields are NOT empty
        if(!getUserName().isEmpty() && !getPassword().isEmpty()){
            // Call client-server handler static signIn function with user-provided username & password
            String response = ClientServerHandler.signIn(getUserName(), getPassword());
            System.out.println(response);
            // If the response is FALSE, show alert. Means username or password error
            if(PlayerInfo.login.equals("false")){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong password or username");
                alert.show();
            }
        }
        else{
            // Show error if no input is given and button is pressed
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please input text before trying to sign in");
            alert.show();
        }
        // Continue by switching scenes upon successful login
    }

    public String getUserName(){return userTxt.getText();}
    public String getPassword(){return passwordField.getText();}
}
