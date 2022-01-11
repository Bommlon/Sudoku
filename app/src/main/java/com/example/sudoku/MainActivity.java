package com.example.sudoku;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[9][9];
    private int selectedNum;
    private int roundCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = findViewById(R.id.linear_00);
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
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        radioGroup.check(R.id.radio_00);
        selectedNum = 1;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                selectedNum = Integer.parseInt(radioButton.getText().toString());
                System.out.println(selectedNum);
            }
        });
        ViewGroup.LayoutParams linearParams = linearLayout.getLayoutParams();
        ViewGroup.LayoutParams radioParams = radioGroup.getLayoutParams();
        int screenWidth = radioParams.width;
        System.out.println("width: "+screenWidth);
        linearParams.height = screenWidth*2;
        linearLayout.setLayoutParams(linearParams);
    }
    @Override
    public void onClick(View v) {
        ((Button) v).setText(Integer.toString(selectedNum));
        roundCount++;
        if (checkForWin()) {
            //TODO: what happens when the player wins
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

    private int[][] generateSudoku(){
        int[][] sudoku = new int[9][9];

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
TODO: Zahlen in jeweils 3 Arrays speichern (Row + Column + 3x3 Area)
TODO: doppelte Zahlen markieren
TODO: Sudoku Test (einfach dank Arrays)
TODO: schick machen (optional)
TODO: Sudoku Generator (optional)
TODO: Sudokus erstellen und teilen (optional)
TODO: Hilfe (optional)
 */