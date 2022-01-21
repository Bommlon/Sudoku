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

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SudokuGenerator sudokuGenerator = new SudokuGenerator();
    private Button[][] buttons = new Button[9][9];
    private int selectedNum;
    private int roundCount;
    private int[][] rows = new int[9][9];
    private int[][] columns = new int[9][9];
    private int[][] blocks = new int[9][9];

    public MainActivity() throws IOException {
    }

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
        generateSudoku();
    }
    @Override
    public void onClick(View v) {
        ((Button) v).setText(Integer.toString(selectedNum));
        System.out.println(((Button) v).getTag().toString());
        roundCount++;
        if (checkForWin()) {
            //TODO: what happens when the player wins
        }
    }

    private boolean checkForWin() { //TODO: an Sudoku anpassen
        int[][] field = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(buttons[i][j].getText().toString().equals("")) return false;
                field[i][j] = Integer.parseInt(buttons[i][j].getText().toString());
            }
        }
        // check rows
        for (int i = 0; i < 9; i++){
            if(containsDuplicates(field[i])){
                return false;
            }
        }
        // check columns
        for (int i = 0; i < 9; i++){
            if(containsDuplicates(field[i])){
                return false;
            }
        }

        return false;
    }

    private boolean containsDuplicates(int[] numbers){
        for (int index = 0; index < numbers.length; index++) {
            int absIndex = Math.abs(numbers[index]);

            if (numbers[absIndex] < 0) {
                return true;

            } else {
                numbers[absIndex] = -numbers[absIndex];
            }
        }
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
                buttons[i][j].setClickable(true);
            }
        }
        generateSudoku();
        roundCount = 0;
        //player1Turn = true;
    }
    private void resetGame() {
        //player1Points = 0;
        //player2Points = 0;
        //updatePointsText();
        resetBoard();
    }

    private void generateSudoku(){
        int[][] sudoku = new int[9][9];
        sudoku = sudokuGenerator.generate(sudoku);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String temp = Integer.toString(sudoku[i][j]);
                buttons[i][j].setText(temp);
                if (temp.equals("0")){
                    buttons[i][j].setText("");
                }
                else {
                    buttons[i][j].setClickable(false);
                }
            }
        }
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