package com.example.tictactoe.controllers;

import animatefx.animation.BounceInDown;
import animatefx.animation.BounceInUp;
import animatefx.animation.FadeIn;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StarterController implements Initializable {
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
  @FXML
  public Button Login;
  @FXML
  public Button Signup;
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    moveX(logo, -90);
    moveY(o1, 50);
    moveY(o2, 100);
    moveY(o3, 200);
    moveY(x1, 50);
    moveY(x2, 100);
    moveY(x3, 200);
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

  public void SwitchToSignUp(ActionEvent event) throws IOException {
    Stage stage;
    Scene scene;
    Parent root;
    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Signup.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    new BounceInUp(root).play();
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
    new BounceInDown(root).play();

  }


}
