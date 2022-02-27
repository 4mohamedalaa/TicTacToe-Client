package com.example.tictactoe;

import animatefx.animation.BounceInLeft;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    Button loginBtn;
    @FXML
    TextField userTxt;
    @FXML
    PasswordField passwordField;
    @FXML
    Button switchToSignUp;
    @FXML
    ImageView logo;
    @FXML
    ImageView o1;
    @FXML
    ImageView o2;
    @FXML
    ImageView o3;
    @FXML
    ImageView x1;
    @FXML
    ImageView x2;
    @FXML
    ImageView x3;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moveX(logo,-90);
        moveY(o1,50);
        moveY(o2,100);
        moveY(o3,200);
        moveY(x1,50);
        moveY(x2,100);
        moveY(x3,200);
        RotateTransition rotate=new RotateTransition();
        rotate.setNode(logo);
        rotate.setDuration(Duration.millis(1000)); // do the rotate in 1 sec
        rotate.setInterpolator(Interpolator.LINEAR); //smooth rotation
        rotate.setByAngle(360);
        rotate.play();

    }
    public  void moveY(Node node,int valueY){
        TranslateTransition translate=new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        // translate.setByX(valueX); // or 250
        translate.setByY(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your  original position after translation
        translate.play();
    }
    public  void moveX(Node node,int valueX){
        TranslateTransition translate=new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        translate.setByX(valueX); // or 250
        //  translate.setByX(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your  original position after translation
        translate.play();
    }
    public void loginBtnAction(ActionEvent event) throws IOException {
        System.out.println("Pressed sign-in button");
        // If the fields are NOT empty
        if (!getUserName().isEmpty() && !getPassword().isEmpty()) {
            // Call client-server handler static signIn function with user-provided username
            // & password
            String response = ClientServerHandler.signIn(getUserName(), getPassword());
            for (PlayerModel player : ClientServerHandler.getOfflinePlayers()) {
                System.out.println(player.getUsername());
            }
            // If the response is FALSE, show alert. Means username or password error
            if (CurrentPlayerModel.login.equals("false")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong password or username");
                alert.show();
            }
        } else {
            // Show error if no input is given and button is pressed
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please input text before trying to sign in");
            alert.show();
        }
        // Continue by switching scenes upon successful login
        if (CurrentPlayerModel.login.equals("true")) {
            Stage stage;
            Scene scene;
            Parent root;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void SwitchToSignUp(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Signup.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        new BounceInLeft(root).play();
    }

    public String getUserName() {
        return userTxt.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }


}
