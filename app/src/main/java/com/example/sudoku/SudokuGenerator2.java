package com.example.sudoku;

public class SudokuGenerator2{
    int[][] grid;
    int difficulty;

    // constructor
    public SudokuGenerator2(){
        this(0);
    }

    public SudokuGenerator2(int d){
        this.grid = new int[9][9];
        this.difficulty = d;
    }

    public static int[][] generate(int difficulty){
        SudokuGenerator2 sudoku = new SudokuGenerator2(difficulty);
        sudoku.grid = sudoku.eraseGrid(sudoku.shuffleGrid(sudoku.fillGrid(sudoku.grid)), difficulty);
        return sudoku.grid;
    }

    // method to fill the grid with values
    public int[][] fillGrid(int[][] g){
        int num =0;                 // input number for field
        int nextRow =3;             // difference between the last number of a row and the first number of the next row is always 4
        int nextRowCounter =0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                num++;
                if(num>9)
                    num -=9;
                g[j][i] = num;
            }
            nextRowCounter++;
            if(nextRowCounter%3==0) // preventing an edge case after every third row
                num++;
            num =(num+nextRow)%9;   // calculation first number of the following row
        }
        return g;
    }

    // method to randomize the grid
    public int[][] shuffleGrid(int[][] g){
        /*  shuffle options:
            change rows,    change groups of rows
            change columns, change groups of columns */
        int randomShuffle =0;   // chooses a shuffle option
        int ra=0;               // random amount of
        int r1=0;               // random pointer1
        int r2=0;               // random pointer2
        int bs=0;               // block-strips (rows or columns)
        for(int c=0; c<10; c++){
            int[][] storage = new int[3][9];
            randomShuffle =(int)(Math.random()*4+1);
            switch(randomShuffle){
                case 1: // change rows
                    bs=(((int)(Math.random()*3))*3);        // output possibilities: {0,3,6}
                    ra=(int)(Math.random()*2+1);            // how often to change a row (1 or 2 times)
                    for(int i=0; i<=ra;i++){
                        r1=bs+((int)(Math.random()*3));       // selected row 1  {0,1,2 or 3,4,5 or 6,7,8}
                        r2=bs+((int)(Math.random()*3));       // selected row 2  {0,1,2 or 3,4,5 or 6,7,8} (same triplet as r1)
                        if(r1!=r2) {
                            storage[0] = g[r1];
                            g[r1] = g[r2];
                            g[r2] = storage[0];
                        }
                    }
                    break;
                case 2: // change columns
                    bs=(((int)(Math.random()*3))*3);        // output possibilities: {0,3,6}
                    ra=(int)(Math.random()*2+1);            // how often to change a row (1 or 2 times)
                    for(int i=0; i<=ra;i++){
                        r1=bs+((int)(Math.random()*3));       // select column 1  {0,1,2 or 3,4,5 or 6,7,8}
                        r2=bs+((int)(Math.random()*3));       // select column 2  {0,1,2 or 3,4,5 or 6,7,8} (same triplet as r1)
                        if(r1!=r2){
                            for(int j=0; j<9;j++){
                                storage[0][j]=g[j][r1];
                                g[j][r1]=g[j][r2];
                                g[j][r2]=storage[0][j];
                            }
                        }
                    }
                    break;
                case 3: // change groups of rows
                    ra=(int)(Math.random()*2+1);
                    for(int i=0; i<=ra;i++){
                        r1=(((int)(Math.random()*3))*3);  // r1,r2 output possibilities: {0,3,6}
                        r2=(((int)(Math.random()*3))*3);  // they are the starting rows/columns of the group
                        if(r1!=r2){
                            for(int k=0;k<3; k++){
                                storage[k]=g[r1+k];
                                g[r1+k]=g[r2+k];
                                g[r2+k]=storage[k];
                            }
                        }
                    }
                    break;
                case 4: // change groups of columns
                    ra=(int)(Math.random()*2+1);
                    for(int i=0; i<=ra;i++){
                        r1=(((int)(Math.random()*3))*3);
                        r2=(((int)(Math.random()*3))*3);
                        if(r1!=r2){
                            for(int j=0; j<9;j++){
                                for(int k=0;k<3; k++){
                                    storage[k][j]=g[j][r1+k];
                                    g[j][r1+k]=g[j][r2+k];
                                    g[j][r2+k]=storage[k][j];
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return g;
    }

    // method to erase a certain number of cells
    public int[][] eraseGrid(int[][] g, int difficulty){
        // difficulty is based on the amount of erased cells
        // ca. 46-51=easy(1), 51-56=intermediate(2), 56-61=difficult(3), [5=for_testing(0)]
        int d=0;
        switch(difficulty){
            case 0:
                d=5;
                break;
            case 2:
                d=((int)((Math.random()*5+1)+50));   // 51-55 = intermediate
                break;
            case 3:
                d=((int)((Math.random()*5+1)+55));   // 56-60 = difficult
                break;
            default:
                d=((int)((Math.random()*5+1)+45));   // 46- 50 = easy
                break;
        }
        // erase blocks
        int r =0;   // rows
        int c =0;   // columns
        while (d>0){
            r=((int)(Math.random()*9));
            c=((int)(Math.random()*9));
            if(g[c][r]!=0){
                g[c][r]=0;
                d--;
            }
        }
        return g;
    }
}
