package com.example.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainGameController implements Initializable {
    @FXML
    public Button mute;
    public ImageView myImage;
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

    public String line = "";
    int counterX = 0;
    int counterO = 0;

    // Image cross = new Image((getClass().getResourceAsStream("CROSS.png")));
    // Image circle = new Image((getClass().getResourceAsStream("CIRCLE.png")));
    // ImageView x = new ImageView(cross);
    // ImageView o = new ImageView(circle);

    private int playerTurn = 0;

    ArrayList<Button> buttons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(
                Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));

        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }
    // Media media = new Media("http://path/file_name.mp3");

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
    }

    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
        filledButtonsCounter = 0;
        button.setStyle("-fx-background-color: #808080 ");

    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
        });
    }

    private int filledButtonsCounter = 0;

    public void setPlayerSymbol(Button button) {
        if (playerTurn % 2 == 0) {
            button.setText("X");
            button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
            button.setTextFill(Color.rgb(255, 0, 0));
            button.setStyle("-fx-background-color: MediumSeaGreen");
            //button.setStyle("-fx-text-fill: Red");

            playerTurn = 1;
            filledButtonsCounter++;
        } else {
            button.setText("O");
            button.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 42));
            button.setTextFill(Color.rgb(255, 255, 0));
            button.setStyle("-fx-background-color: Aqua");
            //button.setStyle("-fx-text-fill: yellow");

            playerTurn = 0;
            filledButtonsCounter++;
        }
    }

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

            } // O winner
            else if (line.equals("OOO")) {
                winnerText.setText("O won!");
                counterO++;
                scoreO.setText(String.valueOf(counterO));
                buttons.forEach(button -> {
                    button.setDisable(true);
                    filledButtonsCounter = 0;
                });
            } else if (filledButtonsCounter == 9) {
                winnerText.setText("Draw");
                filledButtonsCounter = 0;
            }
        }
    }

    Image image = new Image((getClass().getResourceAsStream("mute.png")));
    Image image1 = new Image((getClass().getResourceAsStream("mute_color.png")));

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

    public void exitGame() {

    }

}
