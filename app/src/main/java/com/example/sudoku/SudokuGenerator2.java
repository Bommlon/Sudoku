package com.example.sudoku;

public class SudokuGenerator2 {
    int[][] grid;
    int difficulty;

    // constructor
    public SudokuGenerator2(){
        this(0);
    }

    public SudokuGenerator2(int d) /* throws IOException */ {
        this.grid = new int[9][9];
        this.difficulty = d;
    }

    // method to fill the grid with values
    public int[][] fillGrid(int[][] g){
        int num =0;
        int nextRow =4;             // difference between the last number of a row and the first number of the next row is always 4
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
        int[][] storage = {{0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}};
        boolean[] shuffled ={false, false, false, false};
        int randomShuffle =0;   // chooses a shuffle option
        int ra=0;               // random amount of
        int r1=0;               // random pointer1
        int r2=0;               // random pointer2
        int rc=10;              // reverse counter (infinite loop protection)
        while((!shuffled[0] || !shuffled[1] || !shuffled[2] || !shuffled[3]) && (rc>0)){ // while all not used once OR 10 times
            randomShuffle = (int)(Math.random()*4+1);
            switch(randomShuffle){
                case 1: // change rows
                    ra=(int)(Math.random()*9+1);
                    for(int i=0; i<=ra;i++){
                        r1=(int)(Math.random()*9);  // selected row 1
                        r2=(int)(Math.random()*9);  // selected row 2
                        for(int j=0; j<9;j++){      // changing positions
                            storage[0][j]=g[r1][j];
                            g[r1][j]=g[r2][j];
                            g[r2][j]=storage[0][j];
                        }
                    }
                    break;
                case 2: // change columns
                    ra=(int)(Math.random()*9+1);
                    for(int i=0; i<=ra;i++){
                        r1=(int)(Math.random()*9);  // select column 1
                        r2=(int)(Math.random()*9);  // select column 2
                        for(int j=0; j<9;j++){
                            storage[0][j]=g[j][r1];
                            g[j][r1]=g[j][r2];
                            g[j][r2]=storage[0][j];
                        }
                    }
                    break;
                case 3: // change groups of rows
                    ra=(int)(Math.random()*3+1);
                    for(int i=0; i<=ra;i++){
                        r1=((int)(Math.random()*3)*3-2);  // r1,r2 output possibilities: {1,4,7}
                        r2=((int)(Math.random()*3)*3-2);  // they are the starting rows/columns of the group
                        for(int j=0; j<9;j++){
                            for(int k=0;k<3; k++){
                                storage[k][j]=g[r1+k][j];
                                g[r1+k][j]=g[r2+k][j];
                                g[r2+k][j]=storage[k][j];
                            }
                        }
                    }
                    break;
                case 4: // change groups of columns
                    ra=(int)(Math.random()*3+1);
                    for(int i=0; i<=ra;i++){
                        r1=((int)(Math.random()*3)*3-2);
                        r2=((int)(Math.random()*3)*3-2);
                        for(int j=0; j<9;j++){
                            for(int k=0;k<3; k++){
                                storage[k][j]=g[j][r1+k];
                                g[j][r1+k]=g[j][r2+k];
                                g[j][r2+k]=storage[k][j];
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            shuffled[randomShuffle-1]=true;
            rc--;
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
            case 1:
                d=((int)((Math.random()*5+1)+50));   // 51-55 = intermediate
                break;
            case 2:
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
