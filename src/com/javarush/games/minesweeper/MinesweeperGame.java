package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countFlags = countMinesOnField;
    private boolean isGameStopped = false;
    private int countClosedTiles = SIDE * SIDE;
    private int score = 0;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {

                setCellValue(y, x, "");

                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(y, x, Color.AQUA);
            }
        }
        //gameField[0][0].isMine = true;
        countMineNeighbors();
        countFlags = countMinesOnField;
        //isGameStopped = false;

    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void  countMineNeighbors(){

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {

                if (!gameField[y][x].isMine) {
                    List<GameObject> list = getNeighbors(gameField[y][x]);

                    for (GameObject a : list) {

                        if (a.isMine) {
                           gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private void openTile ( int x, int y){

        if (isGameStopped) {return;}
        if (gameField[x][y].isFlag) {return;}
        if (gameField[x][y].isOpen) {return;}

        //if (!gameField[x][y].isOpen && !gameField[x][y].isFlag && !isGameStopped) {

            if (gameField[x][y].isMine & !gameField[x][y].isOpen) {

                //setCellValue(x, y, MINE);
                gameField[x][y].isOpen = true;
                setCellValueEx(x, y, Color.RED, MINE);
                //setCellColor(x, y, Color.RED);
                gameOver( );
            } else if (gameField[x][y].countMineNeighbors > 0 & !gameField[x][y].isOpen) {
                setCellNumber(x, y, gameField[x][y].countMineNeighbors);
                gameField[x][y].isOpen = true;
                setCellColor(x, y, Color.GREEN);
                score = score + 5;
                setScore(score);

            } else if (gameField[x][y].countMineNeighbors == 0 & !gameField[x][y].isOpen) {

                setCellValue(x, y, "");
                gameField[x][y].isOpen = true;
                setCellColor(x, y, Color.GREEN);

                for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                    openTile(gameObject.x, gameObject.y);
                }

                score = score + 5;
                setScore(score);
            }
        //}

        if (gameField[x][y].isOpen){
            countClosedTiles--;
        }

        if (countClosedTiles == countMinesOnField && !gameField[x][y].isMine){
            win();
        }
    }

    /*
    private void openTile (int x, int y){
        if (gameField[x][y].isMine){
            setCellValue(x, y, MINE);
            gameField[x][y].isOpen = true;
            setCellColor(x, y, Color.RED);
        }
        else {
            if (gameField[x][y].countMineNeighbors > 0){
                setCellNumber(x, y, gameField[x][y].countMineNeighbors);
                gameField[x][y].isOpen = true;
                setCellColor(x, y, Color.GREEN);
            }
            else {
                gameField[x][y].isOpen = true;
                setCellColor(x, y, Color.GREEN);
            }
;
        }
    }
*/
    public void onMouseLeftClick(int a, int b){

        if (isGameStopped){
            restart();
        }
        else {
            openTile(a, b);
        }
    }

    public void onMouseRightClick(int a, int b){
        markTile(a,b);
    }

    private void markTile(int x, int y){

        if (!isGameStopped) {

            if ((!gameField[x][y].isOpen && countFlags > 0) && !gameField[x][y].isFlag) {

                gameField[x][y].isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.YELLOWGREEN);
            } else if (gameField[x][y].isFlag) {
                gameField[x][y].isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.AQUA);
            }
        }

    }

    private void gameOver(){

        isGameStopped = true;
        showMessageDialog(Color.RED, "Game Over", Color.BLACK, 50);
    }

    private void win(){

        isGameStopped = true;
        showMessageDialog(Color.AZURE, "CONGRITULATIONS", Color.BLUEVIOLET, 50);

    }

    private void restart(){

        isGameStopped = false;
        countClosedTiles = SIDE*SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;

        createGame();
    }

}
