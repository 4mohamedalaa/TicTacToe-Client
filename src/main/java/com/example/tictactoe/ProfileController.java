package com.example.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.example.tictactoe.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    Label username;


    public void DisplayUsername (String name){
        username.setText(name);
    }
}
