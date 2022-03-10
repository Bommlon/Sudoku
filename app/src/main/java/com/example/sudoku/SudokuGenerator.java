package com.example.sudoku;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.io.BufferedReader;

public class SudokuGenerator {
    private static int[][] a;
    private static int counter, k1, k2;
    private static BufferedReader obj;
    // stolen from https://arunabhghosal.wordpress.com/2015/04/26/generating-sudoku-puzzle/

    public SudokuGenerator() throws IOException {
        a = new int[9][9];
        counter = 1;
        obj = new BufferedReader(new InputStreamReader(System.in));
    }

    public static int[][] generate(int useless) {
        a = new int[9][9];
        int k = 1, n = 1;
        for (int i = 0; i < 9; i++) {
            k = n;
            for (int j = 0; j < 9; j++) {
                if (k <= 9) {
                    a[i][j] = k;
                    k++;
                } else {
                    k = 1;
                    a[i][j] = k;
                    k++;
                }
            }
            n = k + 3;
            if (k == 10)
                n = 4;
            if (n > 9)
                n = (n % 9) + 1;
        }
        random_gen(1);
        random_gen(0);

        Random rand = new Random();
        int m[] = {0, 3, 6};
        for (int i = 0; i < 2; i++) {
            k1 = m[rand.nextInt(m.length)];
            do {
                k2 = m[rand.nextInt(m.length)];
            } while (k1 == k2);
            if (counter == 1)
                row_change(k1, k2);
            else
                col_change(k1, k2);
            counter++;
        }
        int max = 8;
        int min = 0;

        // Striking out
        for (k1 = 0; k1 < 9; k1++) {
            for (k2 = 0; k2 < 9; k2++)
                strike_out(k1, k2);
        }
        return a;
    }

    public static void random_gen(int check) {
        int k1, k2, max = 2, min = 0;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            k1 = r.nextInt(max - min + 1) + min;
            do {
                k2 = r.nextInt(max - min + 1) + min;
            } while (k1 == k2);
            max += 3;
            min += 3;
            if (check == 1)
                permutation_row(k1, k2);
            else if (check == 0)
                permutation_col(k1, k2);
        }
    }

    public static void permutation_row(int k1, int k2) {
        int temp;
        for (int j = 0; j < 9; j++) {
            temp = a[k1][j];
            a[k1][j] = a[k2][j];
            a[k2][j] = temp;
        }
    }

    public static void permutation_col(int k1, int k2) {
        int temp;
        for (int j = 0; j < 9; j++) {
            temp = a[j][k1];
            a[j][k1] = a[j][k2];
            a[j][k2] = temp;
        }
    }

    public static void row_change(int k1, int k2) {
        int temp;
        for (int n = 1; n <= 3; n++) {
            for (int i = 0; i < 9; i++) {
                temp = a[k1][i];
                a[k1][i] = a[k2][i];
                a[k2][i] = temp;
            }
            k1++;
            k2++;
        }
    }

    public static void col_change(int k1, int k2) {
        int temp;
        for (int n = 1; n <= 3; n++) {
            for (int i = 0; i < 9; i++) {
                temp = a[i][k1];
                a[i][k1] = a[i][k2];
                a[i][k2] = temp;
            }
            k1++;
            k2++;
        }
    }

    public static void strike_out(int k1, int k2) {
        int row_from;
        int row_to;
        int col_from;
        int col_to;
        int i, j, b, c;
        int rem1, rem2;
        int flag;
        int temp = a[k1][k2];
        int count = 9;
        for (i = 1; i <= 9; i++) {
            flag = 1;
            for (j = 0; j < 9; j++) {
                if (j != k2) {
                    if (i != a[k1][j]) {
                        continue;
                    } else {
                        flag = 0;
                        break;
                    }
                }
            }
            if (flag == 1) {
                for (c = 0; c < 9; c++) {
                    if (c != k1) {
                        if (i != a[c][k2]) {
                            continue;
                        } else {
                            flag = 0;
                            break;
                        }
                    }
                }
            }
            if (flag == 1) {
                rem1 = k1 % 3;
                rem2 = k2 % 3;
                row_from = k1 - rem1;
                row_to = k1 + (2 - rem1);
                col_from = k2 - rem2;
                col_to = k2 + (2 - rem2);
                for (c = row_from; c <= row_to; c++) {
                    for (b = col_from; b <= col_to; b++) {
                        if (c != k1 && b != k2) {
                            if (i != a[c][b])
                                continue;
                            else {
                                flag = 0;
                                break;
                            }
                        }
                    }
                }
            }
            if (flag == 0)
                count--;
        }
        if (count == 1) {
            a[k1][k2] = 0;
            count--;
        }
    }
}


