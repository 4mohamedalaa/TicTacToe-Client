package com.example.tictactoe;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

public class HelloApplication extends Application {
    static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        music();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/starter.fxml")));
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        new FadeIn(root).play();
    }

   static public void music() {
        String s = "playplay.mp3";
        Media h = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.play();
    }
    static public void Stopmusic() {
        mediaPlayer.stop();
    }
    static public void Playmusic(){
        mediaPlayer.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


