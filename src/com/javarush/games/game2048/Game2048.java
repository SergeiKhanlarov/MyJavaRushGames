package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

import java.util.Arrays;
import java.util.SimpleTimeZone;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private boolean isGameStopped = false;
    private int score;

    private int gameField[][];

    @Override
    public void initialize() {
        setScreenSize(SIDE,SIDE);
        super.initialize( );
        createGame();
        drawScene();
    }
    private void createGame(){
        gameField = new int[SIDE][SIDE];
        score=0;
        setScore(score);
        createNewNumber();
        createNewNumber();
    }
    private void drawScene(){
        for (int i=0; i<SIDE;i++){
            for (int j=0; j<SIDE; j++){
                setCellColoredNumber(j,i,gameField[i][j]);
            }
        }
    }
    private void createNewNumber(){

        if (getMaxTileValue()==2048){
            win();
        }

        boolean found = false;
        do {
            int x = getRandomNumber(SIDE);
            int y = getRandomNumber(SIDE);
            if (gameField[x][y] == 0) {
                gameField[x][y] = getRandomNumber(10) == 9 ? 4 : 2;
                found = true;
            }
        }while (!found);
    }
    private Color getColorByValue(int value){
        switch (value){
            case 0:return Color.YELLOW;
            case 2:return Color.BLUE;
            case 4:return Color.AQUAMARINE;
            case 8:return Color.CORAL;
            case 16:return Color.DEEPSKYBLUE;
            case 32:return Color.RED;
            case 64:return Color.GREEN;
            case 128:return Color.DARKCYAN;
            case 256:return Color.DARKGREEN;
            case 512:return Color.DARKRED;
            case 1024:return Color.ORANGE;
            case 2048:return Color.OLIVE;
            default:return Color.YELLOW;
        }
    }
    private void setCellColoredNumber(int x, int y, int value){
        setCellValueEx(x,y,getColorByValue(value), value == 0 ? "" : Integer.toString(value));

    }
    private boolean compressRow(int[] row){
        boolean checked = false;
        int temp;
        for (int i=0;i<row.length-1; i++){
            for (int j=0;j<row.length-1; j++){
                if ((row[j] == 0) & (row[j+1]!=0)){
                    temp = row[j];
                    row[j] = row[j+1];
                    row[j+1] = temp;
                    checked = true;
                }
            }
        }
        return checked;
    }

    private boolean mergeRow(int[] row){
        boolean chacked=false;
        for (int i=0;i<row.length-1; i++){
            if ((row[i]!=0)&(row[i] == row[i+1])){
                row[i]=row[i]+row[i+1];
                score += row[i];
                setScore(score);
                row[i+1]=0;
                i++;
                chacked=true;
            }
        }
        return chacked;
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped){
            if (key == Key.SPACE){
             isGameStopped = false;
             createGame();
             drawScene();
            }
        } else if (canUserMove()){
            switch (key) {
                case LEFT:
                        moveLeft( );
                        drawScene( );
                        break;
                case RIGHT:
                        moveRight( );
                        drawScene( );
                        break;
                case DOWN:
                        moveDown( );
                        drawScene( );
                        break;
                case UP:
                        moveUp( );
                        drawScene( );
                        break;
                default:break;
            }
        } else gameOver();
    }
    private void moveLeft(){
        boolean move=false;
        for(int j = 0; j < SIDE; j++){
            if (compressRow(gameField[j])) move=true;
            if (mergeRow(gameField[j])){
                move=true;
                compressRow((gameField[j]));
            }
        }
        if(move) createNewNumber();
    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

    }
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void rotateClockwise(){
        int[][] matrix =  new int[gameField.length][];

        for (int i = 0; i <matrix.length;i++){
                matrix[i] = gameField[i].clone();
        }
        for (int i=0;i<matrix.length; i++){
            for (int j=0;j<matrix.length; j++){
                gameField[j][i] = matrix[matrix.length-i-1][j];
            }
        }
    }
    private int getMaxTileValue(){
        int maxValue = 0;
        for (int[] row: gameField) {
            for (int num: row){
                if (num > maxValue) maxValue=num;
            }
        }
        return maxValue;
    }

    private void win(){
        isGameStopped=true;
        showMessageDialog(Color.BLUE, "YOU WON!", Color.RED,100);
    }
    private boolean canUserMove(){
        boolean check=false;
        for (int[] row: gameField) {
            for (int num: row){
                if (num == 0) check=true;
            }
        }
        if (!check) {
            for (int i=0;i<gameField.length; i++){
                for (int j=0;j<gameField.length; j++){
                    if (i+1 < gameField.length) {
                        if(gameField[i][j] == gameField[i+1][j]) check=true;
                    }
                    if (j+1 < gameField.length){
                        if(gameField[i][j] == gameField[i][j+1]) check=true;
                    }
                }
            }
        }
        return check;
    }

    private void gameOver(){
        isGameStopped=true;
        showMessageDialog(Color.RED, "YOU LOSE", Color.BLACK, 100);
    }
}
