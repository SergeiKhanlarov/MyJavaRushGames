package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {

    private Snake snake;
    private Apple apple;
    private int turnDelay;
    private boolean isGameStopped;
    private static final int GOAL = 28;


    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private int score;

    public void initialize(){
        setScreenSize(WIDTH, HEIGHT);

        createGame();
    }

    private void createGame(){
        snake = new Snake(WIDTH/2, HEIGHT/2);
        score = 0;
        setScore(score);
        //apple = new Apple(5, 5);
        isGameStopped=false;
        createNewApple();
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
        //Apple apple = new Apple(7, 7);
        //apple.draw(this);
    }
    private void drawScene(){
        for (int i=0 ; i < WIDTH ; i++){
            for (int j=0 ; j < HEIGHT ; j++){
                setCellValueEx(i, j , Color.BISQUE, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();
        if(!apple.isAlive) {
            createNewApple();
            score=score+5;
            setScore(score);
            turnDelay=turnDelay-10;
            setTurnTimer(turnDelay);
        }
        //super.onTurn(step);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key){
            case RIGHT: snake.setDirection(Direction.RIGHT);
            break;
            case LEFT: snake.setDirection(Direction.LEFT);
            break;
            case UP: snake.setDirection(Direction.UP);
            break;
            case DOWN: snake.setDirection(Direction.DOWN);
            break;
            case SPACE: if(isGameStopped) createGame();
            break;
        }
    }
    private void createNewApple (){
        do{
            apple = new Apple(getRandomNumber(WIDTH),getRandomNumber(HEIGHT));
        }while (snake.checkCollision(apple));
    }

    private void gameOver(){
        stopTurnTimer();
        isGameStopped=true;
        showMessageDialog(Color.AZURE, "GAME OVER", Color.RED, 100);

    }
    private void win(){
        stopTurnTimer();
        isGameStopped=true;
        showMessageDialog(Color.BEIGE,"YOU WON!", Color.ROYALBLUE, 100);
    }
}
