module com.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;
    requires com.google.gson;



    opens com.example.tictactoe to javafx.fxml;
    exports com.example.tictactoe;

}