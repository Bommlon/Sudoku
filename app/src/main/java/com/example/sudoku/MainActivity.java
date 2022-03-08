package com.example.sudoku;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SudokuGenerator sudokuGenerator = new SudokuGenerator();
    private Button[][] buttons = new Button[9][9];
    private int selectedNum;
    private int roundCount; // number of games won
    private HashMap<Button,Button> errorPairs = new HashMap<Button,Button>();
    private int[][] rows = new int[9][9];
    private int[][] columns = new int[9][9];
    private int[][] blocks = new int[9][9];
    /*
    Block IDs:
     0 | 1 | 2      012
    ---+---+---     345
     3 | 4 | 5      678
    ---+---+---
     6 | 7 | 8

     buttons[down][across]
     */

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
                resetBoard();
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
        /*
        // change height of grid
        ViewGroup.LayoutParams linearParams = linearLayout.getLayoutParams();
        ViewGroup.LayoutParams radioParams = radioGroup.getLayoutParams();
        int screenWidth = radioParams.width;
        System.out.println("width: "+screenWidth);
        linearParams.height = screenWidth*3;
        linearLayout.setLayoutParams(linearParams);
         */
        generateSudoku();
    }
    @Override
    public void onClick(View v) {
        ((Button) v).setText(Integer.toString(selectedNum));
        String name = ((Button) v).getResources().getResourceEntryName(((Button) v).getId());
        int rowNum = name.charAt(7) - '0';
        int columnNum = name.charAt(8) - '0';
        int value = Integer.parseInt(((Button) v).getText().toString());
        placeNum(value, rowNum, columnNum);
        if (checkForWin()) {
            playerWins();
        }
    }

    private void placeNum(int value, int rowNum, int columnNum){
        rows[rowNum][columnNum] = value;
        columns[columnNum][rowNum] = value;

        int blockColumn = columnNum / 3; // 0, 1 or 2
        int blockRow = rowNum / 3;
        int blockNum = blockColumn + (3 * blockRow);

        int tinyBlockColumn = columnNum;
        if (blockColumn == 1) tinyBlockColumn -= 3;
        if (blockColumn == 2) tinyBlockColumn -= 6;
        int tinyBlockRow = rowNum;
        if (blockRow == 1) tinyBlockRow -= 3;
        if (blockRow == 2) tinyBlockRow -= 6;
        int tinyBlockNum = tinyBlockColumn + (3 * tinyBlockRow);
        blocks[blockNum][tinyBlockNum] = value;

        //System.out.println("value: " + value + ", column: " + columnNum + ", row: " + rowNum + ", block: " + blockNum + ", block pos: " + tinyBlockNum);
    }

    private boolean checkForWin() {
        // check if filled
        boolean isfilled = true;
        int[] dupe;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(buttons[i][j].getText().toString().equals("")) isfilled = false;
            }
        }
        System.out.println("---------------------------------");
        // check rows
        System.out.println("checking rows");
        for (int i = 0; i < 9; i++){
            dupe = containedDuplicate(rows[i]);
            if(dupe != null){
                System.out.println("row" + i + " " + Arrays.toString(dupe));
                errorPairs.put(buttons[i][dupe[0]], buttons[i][dupe[1]]);
            }
        }
        // check columns
        System.out.println("checking columns");
        for (int i = 0; i < 9; i++){
            dupe = containedDuplicate(columns[i]);
            if(dupe != null){
                System.out.println("column" + i + " " + Arrays.toString(dupe));
                errorPairs.put(buttons[dupe[0]][i], buttons[dupe[1]][i]);
            }
        }
        // check blocks
        System.out.println("checking blocks");
        for (int i = 0; i < 9; i++){
            dupe = containedDuplicate(blocks[i]);
            if(dupe != null){
                System.out.println("block" + i + " " + Arrays.toString(dupe));
                int tempCol = (i % 3) * 3;
                int tempRow = (i / 3) * 3;

                int colErr0 = tempCol + ((dupe[0] % 3));
                int rowErr0 = tempRow + ((dupe[0] / 3));
                int colErr1 = tempCol + ((dupe[1] % 3));
                int rowErr1 = tempRow + ((dupe[1] / 3));

                System.out.println(tempCol +" "+ tempRow +" | "+ colErr0 +" "+ rowErr0 +" "+ colErr1 +" "+ rowErr1);
                errorPairs.put(buttons[rowErr0][colErr0], buttons[rowErr1][colErr1]);
            }
        }

        if (isfilled && errorPairs.isEmpty()){
            roundCount++;
            return true;
        }
        markErrors();
        return false;
    }

    private boolean containsDuplicates(int[] numbers){
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1 ; j < numbers.length; j++) {
                if ((numbers[i] != 0) && (numbers[j] != 0) && (numbers[i] == (numbers[j]))) {
                    // got the duplicate elements
                    return true;
                }
            }
        }
        return false;
    }

    // new fancy duplicate finder
    private int[] containedDuplicate(int[] numbers){
        LinkedList<Integer> nums = new LinkedList<Integer>();
        int[] res = new int[2];
        for (int i:numbers) nums.add(i);
        int testloc = 0;
        while (nums.size() > 0){
            int test = nums.getFirst();
            nums.removeFirst();
            testloc++;
            if (nums.contains(test) && (test > 0)){
                res[0] = testloc - 1;
                res[1] = testloc + nums.indexOf(test);
                return res;
            }
        }
        return null;
    }

    private void markErrors(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].setTextColor(Color.BLACK);
            }
        }
        for (Button b : errorPairs.keySet()) b.setTextColor(Color.RED);
        for (Button b : errorPairs.values()) b.setTextColor(Color.RED);
        errorPairs.clear();
    }

    private void playerWins() {
        Toast.makeText(this, "nice work!", Toast.LENGTH_LONG).show();
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].setClickable(true);
                buttons[i][j].setTextColor(Color.BLACK);
                buttons[i][j].setTextSize(15);
                buttons[i][j].setTypeface(buttons[i][j].getTypeface(), Typeface.NORMAL);
            }
        }
        System.out.println("\n============= RESET =============\n\n");
        generateSudoku();
    }

    private void generateSudoku(){
        int[][] sudoku = new int[9][9];
        sudoku = sudokuGenerator.generate(sudoku);
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                int value = sudoku[row][column];
                String temp = Integer.toString(value);
                buttons[row][column].setText(temp);
                placeNum(value, row, column);
                if (temp.equals("0")){
                    buttons[row][column].setText("");
                }
                else {
                    buttons[row][column].setClickable(false);
                    buttons[row][column].setTypeface(buttons[row][column].getTypeface(), Typeface.BOLD);
                    buttons[row][column].setTextSize(19);
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