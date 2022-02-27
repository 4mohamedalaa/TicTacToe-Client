package com.example.tictactoe.models;

import java.util.Random;

public class PlayerAI {
    String type;
    private int x, y;
    int[][] board;

    static class Move
    {
        int row, col;
    };

    public PlayerAI(String type) {
        this.type = type;
    }

    public void computerMove(int[][] board) {
        this.board = board;
        Move selectedMove = findBestMove(board);
        this.x = selectedMove.row;
        this.y = selectedMove.col;
    }

    void getRandomMove() {
        Random rand = new Random();
        do {
            this.x = rand.nextInt(3);
            this.y = rand.nextInt(3);
        } while (board[x][y] != 0);
    }

    void bestMove() {
        int bestFinalScore = -10000000;
        Move bestMove = new Move();


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {

                    board[i][j] = 2;
                    int score = minimax(board,0, false);
                    board[i][j] = 0;
                    Math.max(score, bestFinalScore);
                }
            }
        }
    }
    static Move findBestMove(int[][] board)
    {
        int bestVal = -100000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;
        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                // Check if cell is empty
                if (board[i][j] == 0)
                {
                    // Make the move
                    board[i][j] = 2;
                    // compute evaluation function for this
                    int moveVal = minimax(board, 0, false);
                    // Undo the move
                    board[i][j] = 0;
                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal)
                    {
                        bestMove.row = i;
                        bestMove.col = j;
                        System.out.println("Best Move : " + i + " " + i );
                        bestVal = moveVal;
                    }
                }
            }

        }

        System.out.printf("The value of the best Move " +
                "is : %d\n\n", bestVal);
        return bestMove;
    }

    static int minimax(int[][] board, int depth, boolean isMaximizing) {
        int score = checkWin(board);
        // If Maximizer or maximizer has won the game
        // return evaluated score
        if (score != 0)
            return score;
        // If there are no more moves and
        // no winner then it is a tie
        if (isMovesLeft(board) == false)
            return 0;

        if(isMaximizing){
            int best = -10000;
            for (int i=0; i<3; i++)
            {
                for(int j=0; j < 3; j++)
                {
                    if(board[i][j] == 0)
                    {
                        board[i][j] = 2;
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = 0;
                    }
                }
            }
            return best;
        }
        else
        {
            int bestInMinimize = 10000;
            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j] == 0)
                    {
                        // Make the move
                        board[i][j] = 1;
                        // Call minimax recursively and choose
                        // the minimum value
                        bestInMinimize = Math.min(bestInMinimize, minimax(board, depth + 1, true));
                        // Undo the move
                        board[i][j] = 0;
                    }
                }
            }
            return bestInMinimize;
        }
    }

    static int checkWin(int b[][])
    {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++)
        {
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2])
            {
                if (b[row][0] == 2)
                    return +10;
                else if (b[row][0] == 1)
                    return -10;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++)
        {
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col])
            {
                if (b[0][col] == 2)
                    return +10;

                else if (b[0][col] == 1)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2])
        {
            if (b[0][0] == 2)
                return +10;
            else if (b[0][0] == 1)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0])
        {
            if (b[0][2] == 2)
                return +10;
            else if (b[0][2] == 1)
                return -10;
        }
        // Else if none of them have won then return 0
        return 0;
    }

    static Boolean isMovesLeft(int b[][])
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (b[i][j] == 0)
                    return true;
        return false;
    }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
