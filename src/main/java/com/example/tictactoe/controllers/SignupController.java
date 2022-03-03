package com.example.tictactoe.controllers;

//

import animatefx.animation.*;
//import animatefx.animation.BounceInUp;
import com.example.tictactoe.ClientServerHandler;
import javafx.event.ActionEvent;
import javafx.animation.*;
//import javafx.animation.Duration;
import javafx.event.EventHandler;
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

public class SignupController implements Initializable {
    @FXML
    TextField usrname;
    @FXML
    PasswordField password;
    @FXML
    PasswordField cpassword;
    @FXML
    Button Signup;
    @FXML
    Button switchToLogin;
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

        moveX(logo, -90);
        moveY(o1, 50);
        moveY(o2, 100);
        moveY(o3, 200);
        moveY(x1, 50);
        moveY(x2, 100);
        moveY(x3, 200);

        RotateTransition rotate = new RotateTransition();
        rotate.setNode(logo);
        rotate.setDuration(Duration.millis(1000)); // do the rotate in 1 sec
        rotate.setInterpolator(Interpolator.LINEAR); // smooth rotation
        rotate.setByAngle(360);
        rotate.play();

    }

    public void moveY(Node node, int valueY) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        // translate.setByX(valueX); // or 250
        translate.setByY(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your original position after translation
        translate.play();
    }

    public void moveX(Node node, int valueX) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000)); // do the translate in 1 sec
        translate.setByX(valueX); // or 250
        // translate.setByX(valueY); // or 250
        translate.setCycleCount(2); // repeat the translate 2 times
        translate.setAutoReverse(true); // return to your original position after translation
        translate.play();
    }

    public void SignupBtnAction(ActionEvent event) throws IOException {
        System.out.println("Pressed action button");
        System.out.println(usrname.getText());
        System.out.println(cpassword.getText());
        System.out.println(password.getText());
        // If the fields are NOT empty && the passwords are similar
        if (getUserName().isEmpty() || getPassword().isEmpty() || getcPassword().isEmpty()) {
            ShowSignUpFailed();
            return;
        }
        if (!(password.getText().equals(cpassword.getText()))) {
            ShowSignUpFailed();
            return;
        }
        // Call client-server handler static signup function with user-provided username
        // & password
        boolean result = ClientServerHandler.signUp(getUserName(), getPassword());
        if (!result) {
            ShowSignUpFailed();
            return;
        }

        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        new BounceInRight(root).play();
    }

    public void SwitchToLogin(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        new BounceInUp(root).play();

    }

    public void ShowSignUpFailed() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Check your data and try again :)", ButtonType.OK);
        alert.getDialogPane().setMinHeight(100);
        alert.getDialogPane().setMinWidth(100);
        alert.setTitle("sign up Failed");
        alert.setResizable(false);
        alert.show();
        //
    }

    public String getUserName() {
        return usrname.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getcPassword() {
        return password.getText();
    }

}
