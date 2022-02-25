package com.example.tictactoe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LoginController {
    @FXML
    Button loginBtn;
    @FXML
    TextField userTxt;
    @FXML
    PasswordField passwordField;
    @FXML
    Button switchToSignUp;

    public void loginBtnAction(ActionEvent event) throws IOException {
        System.out.println("Pressed sign-in button");
        // If the fields are NOT empty
        if (!getUserName().isEmpty() && !getPassword().isEmpty()) {
            // Call client-server handler static signIn function with user-provided username
            // & password
            String response = ClientServerHandler.signIn(getUserName(), getPassword());
            for (PlayerModel player : ClientServerHandler.getOnlinePlayers()) {
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
    }

    public String getUserName() {
        return userTxt.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
