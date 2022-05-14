package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

import static com.javarush.games.snake.SnakeGame.*;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;
    private List<GameObject> snakeParts = new ArrayList<>();

    public Snake(int x, int y){
        //GameObject s1 = new GameObject(x,y);
        //GameObject s2 = new GameObject(x+1,y);
        //GameObject s3 = new GameObject(x+2, y);
        snakeParts.add(new GameObject(x,y));
        snakeParts.add(new GameObject(x+1,y));
        snakeParts.add(new GameObject(x+2, y));

    }

    public void draw(Game game){

        for (int i = 0; i < snakeParts.size(); i++)
        game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE,
                i==0 ? HEAD_SIGN : BODY_SIGN, isAlive ? Color.CHOCOLATE : Color.RED , 75);
    }

    public void setDirection(Direction direction) {

        if ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && (snakeParts.get(0).x == snakeParts.get(1).x )){
            return;
        }

        if ((this.direction == Direction.UP || this.direction == Direction.DOWN) && (snakeParts.get(0).y == snakeParts.get(1).y)){
            return;
        }
        switch (direction){
            case DOWN: if (this.direction != Direction.UP) this.direction = Direction.DOWN; break;
            case UP: if (this.direction != Direction.DOWN) this.direction = Direction.UP; break;
            case RIGHT: if (this.direction != Direction.LEFT) this.direction = Direction.RIGHT; break;
            case LEFT: if (this.direction != Direction.RIGHT) this.direction = Direction.LEFT; break;
        }
    }

    public void move(Apple apple){
        GameObject newHead = createNewHead();
        if ((newHead.x < 0 | newHead.x > WIDTH - 1) | (newHead.y < 0 | newHead.y > HEIGHT - 1)) {
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        } else snakeParts.add(0, newHead);

        if (apple.x == newHead.x & apple.y == newHead.y) {
            apple.isAlive = false;
        } else {
            removeTail( );
        }
    }
    public GameObject createNewHead(){
        switch (direction){
            case LEFT: return new GameObject(snakeParts.get(0).x-1, snakeParts.get(0).y);
            case UP: return new GameObject(snakeParts.get(0).x, snakeParts.get(0).y-1);
            case DOWN: return new GameObject(snakeParts.get(0).x, snakeParts.get(0).y+1);
            case RIGHT: return new GameObject(snakeParts.get(0).x+1, snakeParts.get(0).y);
            default: return null;
        }
    }
    public void removeTail(){
        snakeParts.remove(snakeParts.size()-1);
    }

    public boolean checkCollision(GameObject gameObject){
        for (GameObject check: snakeParts) {
            if (check.x == gameObject.x & check.y == gameObject.y){
                return true;
            }
        }
        return false;
    }

    public int getLength(){
        return snakeParts.size();
    }
}
