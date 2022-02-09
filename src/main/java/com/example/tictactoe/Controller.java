package com.example.tictactoe;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller  {


    @FXML
    public Button mute;
    public ImageView myimage;

    Image image = new Image((getClass().getResourceAsStream("mute.png")));
    Image image1 = new Image((getClass().getResourceAsStream("mute_color.png")));
    private boolean change=true;

    @FXML
    public void displayImage(){
       if (change) {
           myimage.setImage(image);
           change=false;
       }else{
           myimage.setImage(image1);
           change=true;
       }
    }


}