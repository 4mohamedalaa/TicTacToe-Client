package com.example.tictactoe.controllers;
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































    private int filledButtonsCounter = 0;
    Image image = new Image((Objects.requireNonNull(getClass().getResourceAsStream("/images/mute.png"))));
    Image image1 = new Image((Objects.requireNonNull(getClass().getResourceAsStream("/images/mute_color.png"))));
    private boolean change = true;

    @FXML
    public void displayImage() {
        if (change) {
            myImage.setImage(image);
            change = false;
        } else {
            myImage.setImage(image1);
            change = true;
        }
    }

    public void SwitchToProfile(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show
                ();
    }
































































    public void sendToOne( ){

//System.out.println
//        ("clicked");
        String msg = txtF.getText();
        if(msg != null ){
            // System.out.println("inside clicked ");
            System.out.println(CurrentPlayerModel.username);
            System.out.println(msg);
            ClientServerHandler.sendMessageToOne(msg, CurrentPlayerModel.username);
        }
    }

    public void displayImage(ActionEvent event) {
    }
}