module com.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.google.gson;
    requires com.jfoenix;
    requires javafx.media;

    opens com.example.tictactoe to javafx.fxml;
    exports com.example.tictactoe;
    exports com.example.tictactoe.controllers;
    opens com.example.tictactoe.controllers to javafx.fxml;
    exports com.example.tictactoe.models;
    opens com.example.tictactoe.models to javafx.fxml;
}