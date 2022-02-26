package com.example.tictactoe.models;

import java.util.Random;

public class PlayerAI {
    String type;
    private int x,y;

    public PlayerAI(String type){
        this.type = type;
    }

     public void computerMove(int[][] board){
        int x = 0;
        int y = 0;
        Random rand = new Random();
        do{
            x = rand.nextInt(3);
            y = rand.nextInt(3);
        }while(board[x][y] != 0);

        this.x = x;
        this.y = y;
    }

    public int getX(){
      return x;
    }

    public int getY(){
        return y;
    }

}
