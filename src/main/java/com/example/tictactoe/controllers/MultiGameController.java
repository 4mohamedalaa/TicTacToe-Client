package com.example.tictactoe.controllers;

import com.example.tictactoe.*;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.tictactoe.HelloApplication.Playmusic;
import static com.example.tictactoe.HelloApplication.Stopmusic;
import static com.example.tictactoe.controllers.LoginController.moveX;

public class MultiGameController implements Initializable {

    public Button button6;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button button5;
    public Button button8;
    public Button button9;
    public Button button1;
    public Button button7;
    public VBox playerX;
    public Text scoreX;
    public Text player1Name ;
    public Text scoreO;
    public Text player2Name ;
    public JFXButton restart;
    public Text winnerText;
    public Button mute;
    public JFXButton exit;
    public Button send;
    public TextField txtF;
    public  TextArea txtA;
    @FXML
    ImageView logomulti;


    private final Stage stage = new Stage();
    public static ArrayList<Button> btns=  new ArrayList<>();
        private int[] marks = {0, 0, 0, 0, 0, 0, 0, 0, 0};          //   0   8   0
        private String playerMark = "X";                            //   0   0   0
        private boolean gameEnded;                                  //   0   0   1
    private int moves;
    private int gameID;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moveX(logomulti, -90);
        //txtA = new TextArea() ;
        gameID = CurrentPlayerModel.gameId;
        System.out.println("assign buttons for this btn array");
        btns.add(button1);btns.add(button2);btns.add(button3);
        btns.add(button4);btns.add(button5);btns.add(button6);
        btns.add(button7);btns.add(button8);btns.add(button9);
        System.out.println("CurrentPlayerModel.playerTurn :  "+ CurrentPlayerModel.playerTurn);
        for (Button bt : btns) {
            bt.setOnAction(event -> {
                if (CurrentPlayerModel.allowFire){
                    System.out.println("CurrentPlayerModel.allowFire : "+CurrentPlayerModel.allowFire);
                    System.out.println(" BUTTON index: "+btns.indexOf(bt));
                    int index = btns.indexOf(bt);
                    System.out.println("indeeeeex : "+index);
                    if (!bt.isDisable() && !gameEnded) {
                        //assigning button X or O and style it
                        bt.setText(getPlayer());
                        if(getPlayer() == "X"){
                            bt.setFont(Font.font("MediumSeaGreen", FontWeight.EXTRA_BOLD, 42));
                            bt.setTextFill(Color.rgb(0, 255, 0));
                            bt.setStyle("-fx-background-color: green");
                        }
                        else{
                            bt.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
                            bt.setTextFill(Color.rgb(255, 0, 0));
                            bt.setStyle("-fx-background-color: MediumSeaGreen");
                        }
                        System.out.println("siggggggn : "+getPlayer());
                        bt.setDisable(true);
                        int sign = (bt.getText().equals("X")) ? 8 : 1;
                        System.out.println("mark : "+sign);
                        toggleTurns();
                        marks[index] = sign;
                        moves++;
                        System.out.println("moveees : "+moves);
                        System.out.println(CurrentPlayerModel.playerTurn);
                        if(CurrentPlayerModel.playerTurn) {
                            ClientServerHandler.play(index, sign);
                            CurrentPlayerModel.playerTurn = false;
                            CurrentPlayerModel.allowFire = false;
                        }
                        System.out.println("play");
                        CheckWinning();
                    }
                }
            });
        }
        System.out.println("-----------------------------------");
        this.stage.setOnCloseRequest((e)->{
            JsonObject closingObj = new JsonObject();
            closingObj.addProperty("type", "client_close_while_playing");
            closingObj.addProperty("opponentId", CurrentPlayerModel.opponentId);
            ClientServerHandler.close(closingObj);
        });
    }
    public void StopMusic(ActionEvent stop){
        Stopmusic();
    }
    public void PlayMusic(ActionEvent Play){
        Playmusic();
    }

    public String getPlayer() {
        return playerMark;
    }
    public void toggleTurns() {
        playerMark = (playerMark.equals("X")) ? "O" : "X";
    }
    private void CheckWinning() {
        if (!(winningRowFounded() || winningColFounded())) {
            if (!(checkTopRight() || checkTopLeft())) {
                checkFoeTie();
            }
        }

    }
    private boolean winningRowFounded() {
        boolean founded = false;
        if (!gameEnded) {
            // 0 1 2
            // 3 4 5
            // 6 7 8
            for (int tile = 0; tile < marks.length - 2; tile += 3) {
                if (marks[tile] == 0 || marks[tile + 1] == 0 || marks[tile + 2] == 0)
                    continue;
                if ((marks[tile] == marks[tile + 1]) && (marks[tile] == marks[tile + 2])) {
                    String wins = (marks[tile] == 8) ? "X" : "O";
                    gameEnding(wins);
                    founded = true;
                    Button[] winningTiles = { btns.get(tile), btns.get(tile + 1), btns.get(tile + 2) };
                    showWinningTiles(winningTiles);
                    ShowWinDialog();

                }
            }

        }
        return founded;
    }
    private boolean winningColFounded() {
        boolean founded = false;
        if (!gameEnded) {
            for (int tile = 0; tile < 3; tile++) {
                if (marks[tile] == 0 || marks[tile + 3] == 0 || marks[tile + 6] == 0)
                    continue;
                if ((marks[tile] == marks[tile + 3]) && (marks[tile] == marks[tile + 6])) {
                    String wins = (marks[tile] == 8) ? "X" : "O";
                    gameEnding(wins);
                    founded = true;
                    Button[] winningTiles = { btns.get(tile), btns.get(tile + 3), btns.get(tile + 6) };
                    showWinningTiles(winningTiles);

                }
            }
        }
        return founded;
    }
    private boolean checkTopRight() {
        boolean founded = false;
        if (!gameEnded) {
            int tile = 0;
            if (marks[tile] != 0) {
                if ((marks[tile] == marks[tile + 4]) && (marks[tile] == marks[tile + 8])) {
                    String wins = (marks[tile] == 8) ? "X" : "O";
                    System.out.println("top right");
                    gameEnding(wins);
                    founded = true;
                    Button[] winningTiles = { btns.get(tile), btns.get(tile + 4), btns.get(tile + 8) };
                    showWinningTiles(winningTiles);

                }
            }
        }
        return founded;
    }
    private boolean checkTopLeft() {
        boolean founded = false;
        if (!gameEnded) {
            int tile = 2;
            if (marks[tile] != 0) {
                if ((marks[tile] == marks[tile + 2]) && (marks[tile] == marks[tile + 4])) {
                    String wins = (marks[tile] == 8) ? "X" : "O";
                    System.out.println("top right");
                    gameEnding(wins);
                    founded = true;
                    Button[] winningTiles = { btns.get(tile), btns.get(tile + 2), btns.get(tile + 4) };
                    showWinningTiles(winningTiles);

                }
            }
        }
        return founded;
    }
    private void checkFoeTie() {
        if (!gameEnded && moves == 9) {
            gameEnded = true;
            System.out.println("tieee");
        }
    }
    private void gameEnding(String wins) {
        gameEnded = true;
        btns.forEach(bt -> {
            bt.setDisable(true);
        });
        resetAllTiles();
        getRestart().setVisible(true);
            if (wins.equals("X") && CurrentPlayerModel.mySign.equals("X")) {
                System.out.println("you won");
                makeFinishGameObj();
                ShowWinDialog();
            }
            else if (wins.equals("O") && CurrentPlayerModel.mySign.equals("O")) {
                System.out.println("you won");
                makeFinishGameObj();
                ShowWinDialog();
            } else {
                System.out.println("you lost");
                ShowLoseDialog();
            }


    }
    public void showWinningTiles(Button[] winningTiles) {
        for (Button tile : winningTiles) {
            tile.setId("winninglabel");
        }
    }
    public void resetAllTiles() {
        for (Button tile : btns) {
            tile.setId("label1");
        }
    }
    public Button getRestart() {
        return restart;
    }
    public void makeFinishGameObj() {
        JsonObject gameFinish = new JsonObject();
        gameFinish.addProperty("type", "finish_game");
        gameFinish.addProperty("winner", CurrentPlayerModel.id);
        gameFinish.addProperty("looser", CurrentPlayerModel.opponentId);
        gameFinish.addProperty("game_id", CurrentPlayerModel.gameId);
        ClientServerHandler.sendFinishingObj(gameFinish);
    }
    public void ShowWinDialog() {
        StackPane secondaryLayout2 = new StackPane();
        MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("/fxml/Winner.mp4").toExternalForm()));
        MediaView mediaView2 = new MediaView(videoForWinner);
        secondaryLayout2.getChildren().addAll(mediaView2);
        Scene secondScene2 = new Scene(secondaryLayout2, 420, 400);
        Stage secondStage2 = new Stage();
        secondStage2.setResizable(false);
        secondStage2.setScene(secondScene2);
        secondStage2.show();
        videoForWinner.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> secondStage2.close() );
        delay.play();
        // MediaPlayer mediaPlayer = new MediaPlayer(new
        // Media(this.getClass().getResource("/Controllers/../ui_modules/Resources/winner.mp4").toExternalForm()));
        // mediaPlayer.setAutoPlay(true);
        // MediaView mediaView = new MediaView(mediaPlayer);
       /* Alert alert = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
        alert.getDialogPane().setMinHeight(600);
        alert.getDialogPane().setMinWidth(600);
        alert.setTitle("You win!!");
        System.out.println("you win **********");*/
        //VBox content = new VBox(mediaView);
        //content.setAlignment(Pos.CENTER);
        //alert.getDialogPane().setContent(content);
        //alert.setOnShowing(e -> mediaPlayer.play());
        //alert.initOwner(stage);
        //alert.show();
    }
    public void ShowLoseDialog() {
        StackPane secondaryLayout2 = new StackPane();
        MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("/fxml/loser.mp4").toExternalForm()));
        MediaView mediaView2 = new MediaView(videoForWinner);
        secondaryLayout2.getChildren().addAll(mediaView2);
        Scene secondScene2 = new Scene(secondaryLayout2, 420, 300);
        Stage secondStage2 = new Stage();
        secondStage2.setResizable(false);
        secondStage2.setScene(secondScene2);
        secondStage2.show();
        videoForWinner.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> secondStage2.close() );
        delay.play();
        // MediaPlayer player = new MediaPlayer(new
        // Media(getClass().getResource("/Controllers/../ui_modules/Resources/losser.mp4").toExternalForm()));
        // MediaView mediaView = new MediaView(player);
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
        alert.getDialogPane().setMinHeight(210);
        alert.getDialogPane().setMinWidth(210);
        alert.setTitle("You lose!!");
        System.out.println("you lose **********");*/
        //VBox content = new VBox(mediaView);
        //content.setAlignment(Pos.CENTER);
        //alert.getDialogPane().setContent(content);
        //alert.setOnShowing(e -> player.play());
        //alert.initOwner(stage);
        //alert.show();
    }
    private EventHandler<ActionEvent> sendToOne(Stage primaryStage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String msg = txtF.getText();
                if (msg != null) {
                    ClientServerHandler.sendMessageToOne(msg, CurrentPlayerModel.opponentUsername);
                }

            }
        };
    }
    public void restartGame(ActionEvent event) {
    }
    public void displayImage(ActionEvent event) {
    }
    public void sendToOne() {
                    String msg =txtF.getText();
                    if(msg != null ){
                        txtA.appendText(msg);
                        txtA.appendText("\n");
                       ClientServerHandler.sendMessageToOne(msg,CurrentPlayerModel.opponentUsername);

                    }
    }
    public static void opponentsMove(int position) {
        System.out.println("opponent"+position);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                CurrentPlayerModel.allowFire = true;
                btns.get(position).fire();
               CurrentPlayerModel.playerTurn=true;

            }
        });
    }





}
































/*
import com.example.tictactoe.*;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.example.tictactoe.models.PlayerAI;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import static java.lang.Thread.sleep;
public class MultiGameController implements Initializable {
    @FXML
    public Button mute;
    @FXML
    public ImageView myImage;
    @FXML
    public Button send;
    @FXML
    public TextArea  txtA;
    @FXML
    public TextField txtF;
    @FXML
    public Button button1;
    @FXML
    public Button button2;
    @FXML
    public Button button3;
    @FXML
    public Button button4;
    @FXML
    public Button button5;
    @FXML
    public Button button6;
    @FXML
    public Button button7;
    @FXML
    public Button button8;
    @FXML
    public Button button9;
    @FXML
    public Text winnerText;
    @FXML
    public VBox playerX;
    @FXML
    public Text scoreO;
    @FXML
    public Text scoreX;
    @FXML
    public JFXButton exit;
    @FXML
    public JFXButton restart;

    @FXML
    public Text player1Name;
    @FXML
    public Text player2Name;

    static class Point {
        int row, col;
        Point(int row, int col) {
            this.col = col;
            this.row = row;
        }
    }

    public String line = "";
    int counterX = 0;
    int counterO = 0;
    private int board[][] = new int[3][3];
    private int movesLeft = 9;
    private boolean gameOver = false;
    public String choice ;
    ArrayList<Button> buttons;
    Map<Button, MultiGameController.Point> btnBoard = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(
                Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));
        int i = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                btnBoard.put(buttons.get(i++), new Point(row, col));
            }
        }
        buttons.forEach(button -> {
            player_turn(button,choice);
            button.setFocusTraversable(false);
        });
    }

    public void opponent_action(JsonObject oponnetmove){
        //here Json received from player opponent button clicked and choice
        String sign = oponnetmove.get("sign").toString();
        int position =  Integer.parseInt(oponnetmove.get("position").toString());
        int oponnent_x = position/10; position = position%10;
        int oponnent_y = position;
        movesLeft--;
        checkIfGameIsOver(); // ? not sure if needed
        if (movesLeft > 1 && !gameOver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    btnBoard.entrySet().forEach(entry -> {
                        if (entry.getValue().row == oponnent_x && entry.getValue().row == oponnent_y) {
                            entry.getKey().setText(sign);
                            entry.getKey().setDisable(true);
                        }
                    });
                }
            });
            board[oponnent_x][oponnent_y] = 1;
        }
    }

    public void player_turn(Button button, String choice) {
        button.setOnMouseClicked(mouseEvent -> {
            {
                if (movesLeft > 1 && !gameOver) {
                    movesLeft--;
                    button.setDisable(true);
                    button.setText("X");
                    Point p = btnBoard.get(button);
                    board[p.row][p.col] = 1;
                    button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
                    button.setTextFill(Color.rgb(255, 0, 0));
                    button.setStyle("-fx-background-color: MediumSeaGreen");
                    checkIfGameIsOver();
                    if (movesLeft > 1 && !gameOver) {
                        JsonObject boardUpdate = new JsonObject();
                        String position = String.valueOf(p.row).concat(String.valueOf(p.col)) ;
                        boardUpdate.addProperty("type", "play");
                        boardUpdate.addProperty("opponent", CurrentPlayerModel.opponentId);
                        boardUpdate.addProperty("position", position);
                        boardUpdate.addProperty("sign", choice);
                        ClientServerHandler.passMoveToOponnent(boardUpdate) ;
                    }
                }
            }
        });
    }




    @FXML
    void restartGame(ActionEvent event) {
        movesLeft = 9;
        gameOver = false;
        for (int[] row : board)
            Arrays.fill(row, 0);
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
    }
    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
        filledButtonsCounter = 0;
        button.setStyle("-fx-background-color: #808080 ");
    }
    /*private void humanTurn(Button button) {
       button.setOnMouseClicked(mouseEvent -> {
           movesLeft--;
           button.setDisable(true);
           button.setText("X");

           MultiGameController.Point p = btnBoard.get(button);
           System.out.println("Human Selected "+p.row + "  " + p.col);
           System.out.println("Human Selected " + button);
           board[p.row][p.col] = 1;
           checkIfGameIsOver();
           if (movesLeft > 1 && !gameOver) {
               aiPlayer.computerMove(board);
               int playedX = aiPlayer.getX();
               int playedY = aiPlayer.getY();
               board[playedX][playedY] = 2;
               System.out.println("Ai Selected " + playedX + " " + playedY);

               for (var entry : btnBoard.entrySet()) {
                   if (entry.getValue().row == playedX && entry.getValue().col == playedY) {
                       System.out.println("Ai Selected " + entry.getKey());
                       try {
                           sleep(500);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       entry.getKey().setText("O");
                       entry.getKey().setDisable(true);
                       break;
                   }
               }
               movesLeft--;
               checkIfGameIsOver();
           }

           JsonObject boardUpdate = new JsonObject();
           boardUpdate.addProperty("type", "oponnetmove");
           boardUpdate.addProperty("movesleft", movesLeft);
           boardUpdate.addProperty("xcoordinate", p.row);
           boardUpdate.addProperty("ycoordinate", p.col);

           ClientServerHandler.passMoveToOponnent();
       });
   }*/
   /*
    public void checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> "null";
            };

            // X winner
            if (line.equals("XXX")) {
                winnerText.setText("X won!");
                counterX++;
                scoreX.setText(String.valueOf(counterX));

                buttons.forEach(button -> {
                    button.setDisable(true);
                    filledButtonsCounter = 0;
                });
                gameOver = true;
            } //O winner
            else if (line.equals("OOO")) {
                winnerText.setText("O won!");
                counterO++;
                scoreO.setText(String.valueOf(counterO));
                buttons.forEach(button -> {
                    button.setDisable(true);
                    filledButtonsCounter = 0;
                });
                gameOver = true;
            }
            if (movesLeft == 0 && !gameOver) {
                winnerText.setText("Draw");
                gameOver = true;
            }
        }
    }




*/












