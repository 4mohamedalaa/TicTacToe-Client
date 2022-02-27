package com.example.tictactoe.controllers;

import com.example.tictactoe.*;
import com.example.tictactoe.models.CurrentPlayerModel;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.tictactoe.ClientServerListener.myControllerHandle2;

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
    public Text player1Name;
    public Text scoreO;
    public Text player2Name;
    public JFXButton restart;
    public Text winnerText;
    public Button mute;
    public JFXButton exit;
    public Button send;
    public TextField txtF;
    public TextArea txtA;
    protected Button record;
    private final Stage stage = new Stage();
    private final ArrayList<Button> btns = new ArrayList<>();
    private boolean opponentsTurn;
    private boolean currentplayerturn;
    private int[] marks = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private String playerMark = "X";
    private boolean gameEnded;
    private int moves;
    private int gameID;

    /*
     * public MultiGameController(ArrayList<Button> btns) {
     * this.btns = btns;
     * stage = null;
     * }
     * public MultiGameController() {
     * stage = null;
     * this.btns = null;
     * }
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameID = CurrentPlayerModel.gameId;
        System.out.println("**********************");
        System.out.println("inside game board");
        System.out.println("**********************");
        //
        System.out.println("assign buttons for this btn array");
        btns.add(button1);
        btns.add(button2);
        btns.add(button3);
        btns.add(button4);
        btns.add(button5);
        btns.add(button6);
        btns.add(button7);
        btns.add(button8);
        btns.add(button9);
        //
        currentplayerturn = CurrentPlayerModel.playerTurn;
        System.out.println("-----------------------------------");
        for (Button bt : btns) {
            bt.setOnAction(event -> {
                if (CurrentPlayerModel.allowFire) {
                    System.out.println(" BUTTON index: " + btns.indexOf(bt));
                    int index = btns.indexOf(bt);
                    bt.setFont(new Font("System Bold Italic", 200));
                    bt.setStyle("-fx-font-size:40");
                    if (!bt.isDisable() && !gameEnded) {
                        bt.setText(getPlayer());
                        bt.setDisable(true);
                        int sign = (bt.getText().equals("X")) ? 8 : 1;
                        toggleTurns();
                        marks[index] = sign;
                        moves++;
                        CheckWinning();
                        if (CurrentPlayerModel.playerTurn) {
                            ClientServerHandler.play(index, sign);
                            CurrentPlayerModel.playerTurn = false;
                            CurrentPlayerModel.allowFire = false;
                        }
                        System.out.println("aplay");
                        currentplayerturn = true;
                        CheckWinning();
                    }
                }
            });

        }
        System.out.println("-----------------------------------");
        myControllerHandle2.stage.setOnCloseRequest((e) -> {
            JsonObject closingObj = new JsonObject();
            closingObj.addProperty("type", "client_close_while_playing");
            closingObj.addProperty("opponentId", CurrentPlayerModel.opponentId);
            ClientServerHandler.close(closingObj);
        });
    }

    public String getPlayer() {
        return playerMark;
    }

    public void toggleTurns() {
        playerMark = (playerMark.equals("X")) ? "O" : "X";
        // infoScreen.changeMsg("player "+player);
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
        getRecord().setVisible(true);

        if (wins.equals("X") && CurrentPlayerModel.mySign.equals("X")) {
            System.out.println("you won");
            makeFinishGameObj();
            ShowWinDialog();
        } else if (wins.equals("O") && CurrentPlayerModel.mySign.equals("O")) {
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

    public Button getRecord() {
        return record;
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
        // MediaPlayer mediaPlayer = new MediaPlayer(new
        // Media(this.getClass().getResource("/Controllers/../ui_modules/Resources/winner.mp4").toExternalForm()));
        // mediaPlayer.setAutoPlay(true);
        // MediaView mediaView = new MediaView(mediaPlayer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
        alert.getDialogPane().setMinHeight(600);
        alert.getDialogPane().setMinWidth(600);
        alert.setTitle("You win!!");
        // VBox content = new VBox(mediaView);
        // content.setAlignment(Pos.CENTER);
        // alert.getDialogPane().setContent(content);
        // alert.setOnShowing(e -> mediaPlayer.play());
        alert.initOwner(stage);
        alert.show();
    }

    public void ShowLoseDialog() {
        // MediaPlayer player = new MediaPlayer(new
        // Media(getClass().getResource("/Controllers/../ui_modules/Resources/losser.mp4").toExternalForm()));
        // MediaView mediaView = new MediaView(player);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
        alert.getDialogPane().setMinHeight(210);
        alert.getDialogPane().setMinWidth(210);
        alert.setTitle("You lose!!");
        // VBox content = new VBox(mediaView);
        // content.setAlignment(Pos.CENTER);
        // alert.getDialogPane().setContent(content);
        // alert.setOnShowing(e -> player.play());
        alert.initOwner(stage);
        alert.show();
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

    public void sendToOne(ActionEvent event) {
    }

}