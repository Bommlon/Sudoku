package com.example.sudoku;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[9][9];
    //private boolean player1Turn = true;
    private int roundCount;
    //private int player1Points;
    //private int player2Points;
    //private TextView textViewPlayer1;
    //private TextView textViewPlayer2;
    private int selectedNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textViewPlayer1 = findViewById(R.id.text_view_p1);
        //textViewPlayer2 = findViewById(R.id.text_view_p2);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        /*
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        */
        roundCount++;
        if (checkForWin()) {
            /*
            if (player1Turn) {
                playerWins();
            } else {
                player2Wins();
            }
             */
        } else if (roundCount == 81) {
            draw();
        } else {
            //player1Turn = !player1Turn;
        }
    }
    private boolean checkForWin() { //TODO: an Sudoku anpassen
        String[][] field = new String[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        /*
        for (int i = 0; i < 9; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
         */
        return false;
    }
    private void playerWins() {
        //player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        //updatePointsText();
        resetBoard();
    }
    /*
    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
     */
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }
    /*
    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }
     */
    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        //player1Turn = true;
    }
    private void resetGame() {
        //player1Points = 0;
        //player2Points = 0;
        //updatePointsText();
        resetBoard();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {   //this is what happens when the app closes TODO: an Sudoku anpassen
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        //outState.putInt("player1Points", player1Points);
        //outState.putInt("player2Points", player2Points);
        //outState.putBoolean("player1Turn", player1Turn);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  //this is what happens when the app opens TODO: an Sudoku anpassen
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        //player1Points = savedInstanceState.getInt("player1Points");
        //player2Points = savedInstanceState.getInt("player2Points");
        //player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}
/*
TODO: Buttons (9x9) anpassen
TODO: 9 Buttons für Auswahl der Zahlen (1x9 oder 3x3? -> testen)
TODO: Zahlen in jeweils 3 Arrays speichern (Row + Column + 3x3 Area)
TODO: doppelte Zahlen markieren
TODO: Sudoku Test (einfach dank Arrays)
TODO: schick machen (optional)
TODO: Sudoku Generator (optional)
TODO: Sudokus erstellen und teilen (optional)
TODO: Hilfe (optional)
 */