package com.example.tictactoe.controllers;

import com.example.tictactoe.models.PlayerAI;
import com.example.tictactoe.models.CurrentPlayerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.example.tictactoe.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class MainGameController implements Initializable {
    @FXML
    public Button mute;
    public ImageView myImage;
    @FXML
    public Button send;
    @FXML
    public TextArea txtA;
    @FXML
    public TextField txtF;
    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    public Text winnerText;
    @FXML
    private Text scoreX;
    @FXML
    private Text scoreO;
    @FXML
    private Button exit;

    @FXML
    private Button restart;

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

    // Image cross = new Image((getClass().getResourceAsStream("CROSS.png")));
    // Image circle = new Image((getClass().getResourceAsStream("CIRCLE.png")));
    // ImageView x = new ImageView(cross);
    // ImageView o = new ImageView(circle);

    private int playerTurn = 0;
    PlayerAI aiPlayer = new PlayerAI("dump");
    ArrayList<Button> buttons;
    Map<Button, Point> btnBoard = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttons = new ArrayList<>(
                Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));

        int i = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                //System.out.println(buttons.get(i));
                btnBoard.put(buttons.get(i++), new Point(row, col));
            }
        }

        for (var entry : btnBoard.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue().row + "/" + entry.getValue().col);
        }

        buttons.forEach(button -> {
            humanTurn(button);
            System.out.println("Human turn");
            button.setFocusTraversable(false);
        });
    }
    // Media media = new Media("http://path/file_name.mp3");

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

    private void humanTurn(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            movesLeft--;
//          setPlayerSymbol(button);
            button.setDisable(true);
            button.setText("X");
            Point p = btnBoard.get(button);
            System.out.println("Human Selected "+p.row + "  " + p.col);
            System.out.println("Human Selected " + button);

            board[p.row][p.col] = 1;
//            button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
//            button.setTextFill(Color.rgb(255, 0, 0));
//            button.setStyle("-fx-background-color: MediumSeaGreen");
            //updateBoard(button);
            checkIfGameIsOver();
            if (movesLeft > 1 && !gameOver) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        System.out.print(board[i][j]);
                    }
                    System.out.println("");
                }
                aiPlayer.computerMove(board);
                int playedX = aiPlayer.getX();
                int playedY = aiPlayer.getY();
                board[playedX][playedY] = 2;
                System.out.println("Ai Selected" + playedX + " " + playedY);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        System.out.print(board[i][j]);
                    }
                    System.out.println("");
                }
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


        });
    }

//    private void computerMove() {
//        int x = 0;
//        int y = 0;
//        Random rand = new Random();
//        do {
//            x = rand.nextInt(3);
//            y = rand.nextInt(3);
//        } while (board[x][y] != 0);
//        board[x][y] = 2;
//
////        if(x == 0 && y == 0)
////        {
////            button1.setText("O");
////            button1.setDisable(true);
////        }
////        else if(x == 0 && y == 1){
////            button2.setText("O");
////            button2.setDisable(true);
////        }
////        else if(x == 0 && y == 2){
////            button3.setText("O");
////            button3.setDisable(true);
////        }
////        else if(x == 1 && y == 0){
////            button4.setText("O");
////            button4.setDisable(true);
////        }
////        else if(x == 1 && y == 1){
////            button5.setText("O");
////            button5.setDisable(true);
////        }
////        else if(x == 1 && y == 2){
////            button6.setText("O");
////            button6.setDisable(true);
////        }
////        else if(x == 2 && y == 0){
////            button7.setText("O");
////            button7.setDisable(true);
////        }
////        else if(x == 2 && y == 1){
////            button8.setText("O");
////            button8.setDisable(true);
////        }
////        else if(x == 2 && y == 2){
////            button9.setText("O");
////            button9.setDisable(true);
////        }
//    }

    //    private void updateBoard(Button b){
//        int x = 0;
//        int y = 0;
//        switch (b.getId()){
//            case "button1":
//                x = 0;
//                y = 0;
//
//                break;
//            case "button2":
//                x = 0;
//                y = 1;
//                break;
//            case "button3":
//                x = 0;
//                y = 2;
//                break;
//            case "button4":
//                x = 1;
//                y = 0;
//                break;
//            case "button5":
//                x = 1;
//                y = 1;
//                break;
//            case "button6":
//                x = 1;
//                y = 2;
//                break;
//            case "button7":
//                x = 2;
//                y = 0;
//                break;
//            case "button8":
//                x = 2;
//                y = 1;
//                break;
//            case "button9":
//                x = 2;
//                y = 2;
//                break;
//        }
//        board[x][y] = 1;
//    }
    private int filledButtonsCounter = 0;

//    public void setPlayerSymbol(Button button) {
//        if (playerTurn % 2 == 0) {
//            button.setText("X");
//            button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
//            button.setTextFill(Color.rgb(255, 0, 0));
//            button.setStyle("-fx-background-color: MediumSeaGreen");
//            // button.setStyle("-fx-text-fill: Red");
//
//            playerTurn = 1;
//            filledButtonsCounter++;
//        } else {
//            button.setText("O");
//            button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
//            button.setTextFill(Color.rgb(255, 255, 0));
//            button.setStyle("-fx-background-color: Aqua");
//            // button.setStyle("-fx-text-fill: yellow");
//
//            playerTurn = 0;
//            filledButtonsCounter++;
//        }
//    }

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
        stage.show();
    }

    public void sendToOne( ){
        //System.out.println("clicked");
        String msg = txtF.getText();
        if(msg != null ){
            // System.out.println("inside clicked ");
            System.out.println(CurrentPlayerModel.username);
            System.out.println(msg);
            ClientServerHandler.sendMessageToOne(msg, CurrentPlayerModel.username);
        }
    }

}
