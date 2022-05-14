package com.javarush.games.minesweeper;

public class GameObject {

    public  boolean isFlag = false;

    public boolean isOpen = false;

    public int countMineNeighbors;

    public boolean isMine;

   public int x,y;

    public GameObject (int x, int y, boolean isMine){

        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }
}
