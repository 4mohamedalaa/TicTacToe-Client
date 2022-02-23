package com.example.tictactoe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.tictactoe.*;

import java.io.IOException;

public class SignupController {

    @FXML
    TextField usrname;

    @FXML
    TextField password;

    @FXML
    TextField cpassword;

    @FXML
    Button Signup;
    @FXML
    Button switchToLogin;
//    public boolean CheckData (String name, String pass, String cpass ){
//        name = usrname.getText();
//        pass = password.getText();
//        cpass = cpassword.getText();
//
//        if (name != null && (cpass.equals(pass))) {
//            return true;}
//        else{
//                ShowSignUpFailed();
//                return false;
//            }
//
//    }
//
//    public void SignupBtn(EventHandler<ActionEvent> Action){
//        Signup.setOnAction(Action);
//    }
//
// void SignupBtn(ActionEvent event){
//
//
//    Signup.setOnAction(new EventHandler<ActionEvent>() {
//
//        @Override
//        public void handle(ActionEvent arg0) {
//            // TODO Auto-generated method stub
//            System.out.println("Button clicked");
//
//        }
//    } );
public void SwitchToLogin(ActionEvent event) throws IOException
{
    Stage stage;
    Scene scene;
    Parent root;
    root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

}



    public void ShowSignUpFailed() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Check your data and try again :)", ButtonType.OK);
        alert.getDialogPane().setMinHeight(100);
        alert.getDialogPane().setMinWidth(100);
        alert.setTitle("sign up Failed");
        alert.setResizable(false);
        alert.show();
    }


}


