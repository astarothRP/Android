package com.astaroth.juegosolitario;

/**
 * Created by rperez on 10/03/2015.
 */
public class Game {
    public static final int SIZE = 7;
    private int grid[][];
    private static final int CROSS[][] = {{0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
    };
    private static final int BOARD[][] = {{0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
    };
    private int picked1, picked2;
    private int jumped1, jumped2;
    private enum State {READY_TO_PICK, READY_TO_DROP, FINISHED}
    private State gameState;

    public Game() {
        grid = new int[SIZE][SIZE];
        for(int f=0; f<SIZE; f++){
            for(int c=0; c<SIZE; c++){
                grid[f][c]=CROSS[f][c];
            }
        }
        gameState = State.READY_TO_PICK;
    }

    private boolean isAvailable(int fFrom, int cFrom, int fTo, int cTo){
        if (grid[fFrom][cFrom]==0 || grid[fTo][cTo]==1) return  false;
        if (cFrom==cTo && Math.abs(fTo-fFrom)==2) {
            jumped1 = fTo>fFrom ? fFrom+1 : fTo+1;
            jumped2 = cFrom;
            if (grid[jumped1][jumped2]==1) return true;
        }
        if (fFrom==fTo && Math.abs(cTo-cFrom)==2) {
            jumped1 = fFrom;
            jumped2 = cTo>cFrom ? cFrom+1 : cTo+1;
            if (grid[jumped1][jumped2]==1) return true;
        }
        return false;
    }

    public int getGrid(int f, int c){ return grid[f][c]; }

    public void play (int f, int c){
        if (gameState==State.READY_TO_PICK){
            picked1 = f;
            picked2 = c;
            gameState=State.READY_TO_DROP;
        } else if (gameState==State.READY_TO_DROP){
            if (isAvailable(picked1, picked2, f, c)){
                gameState = State.READY_TO_PICK;
                grid[picked1][picked2]=0;
                grid[jumped1][jumped2]=0;
                grid[f][c]=1;
                if (isGameFinished()){
                    gameState=State.FINISHED;
                }
            } else {
                picked1 = f;
                picked2 = c;
            }
        }
    }

    public boolean isPicked(int f, int c){
        if (gameState==State.READY_TO_DROP && picked1 == f && picked2 == c) return  true;
        else return false;
    }
    public boolean isGameFinished(){
        for(int f=0; f<SIZE; f++) {
            for (int c = 0; c < SIZE; c++) {
                for (int p = 0; p < SIZE; p++) {
                    for (int q = 0; q < SIZE; q++) {
                        if (grid[f][c]==1 && grid[p][q]==0 && BOARD[p][q]==1 && isAvailable(f, c, p, q)) return false;
                    }
                }
            }
        }
        return true;
    }
}
