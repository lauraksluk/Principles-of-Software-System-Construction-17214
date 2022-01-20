package edu.cmu.cs214.hw3.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class that represents the Santorini game board (5x5 grid).
 * Handles the logic of building towers from blocks/dome with an integer matrix,
 * with each board[x][y] value representing the height of the "tower" at (x,y).
 */
public class Board {
    public static final int BOARD_WIDTH = 5;
    public static final int BOARD_HEIGHT = 5;
    public static final int MAX_HEIGHT = 4;
    public static final int WIN_HEIGHT = 3;
    private int[][] gameGrid;
    private Worker workerJustMoved;
    private List<List<Worker>> workers;

    /**
     * Constructor for creating a new 5x5 board.
     */
    public Board() {
        gameGrid = new int[BOARD_WIDTH][BOARD_HEIGHT];
        workers = new ArrayList<>();
        workers.add(new ArrayList<>(2));
        workers.get(0).add(new Worker(0));
        workers.get(0).add(new Worker(1));
        workers.add(new ArrayList<>(2));
        workers.get(1).add(new Worker(0));
        workers.get(1).add(new Worker(1));
    }

    public Worker getWorker(int playerId, int workerId) {
        return workers.get(playerId).get(workerId);
    }

    public List<List<Worker>> getWorkers() {
        return workers;
    }

    /**
     * Method to place both workers at the beginning of the game.
     * @param playerId current player
     * @param workerId worker to place
     * @param x1 x-coordinate to place worker
     * @param y1 y-coordinate to place worker
     * @return true, if successfully placed, false otherwise
     */
    public boolean initializeWorkers(int playerId, int workerId, int x1, int y1) {
        if (isOccupied(x1, y1)) {
            throw new IllegalArgumentException("position is occupied");
        }
        if (!inBounds(x1, y1)) {
            return false;
        }
        Worker w = workers.get(playerId).get(workerId);
        w.setPosition(x1, y1);
        return true;
    }

    public boolean isOccupied(int x, int y) {
        List<List<Worker>> workers = getWorkers();
        for (int i = 0; i < workers.size(); i++) {
            for (int j = 0; j < workers.get(0).size(); j++) {
                Worker w = workers.get(i).get(j);
                int[] wPosition = w.getPosition();
                if (wPosition[0] == x && wPosition[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean inBounds(int x1, int y1) {
        return 0 <= x1 && x1 < BOARD_WIDTH && 0 <= y1 && y1 < BOARD_HEIGHT;
    }

    /**
     * Method that returns the current playing game grid/board.
     * @return playing board
     */
    public int[][] getGameGrid() {
        return gameGrid;
    }

    /**
     * Method that returns the number of levels/blocks the tower(grid space) at
     * position (x,y) has.
     * @param x x-coordinate value
     * @param y y-coordinate value
     * @return number of blocks on position
     */
    public int getCurrentHeight(int x, int y) {
        return gameGrid[x][y];
    }

    /**
     * Method that adds 1 block to the game board grid at (x,y).
     * If the position at (x,y) already is a complete tower, does nothing.
     * @param x x-coordinate value
     * @param y y-coordinate value
     */
    public void addBlock(int x, int y) {
        if (gameGrid[x][y] < MAX_HEIGHT) {
            gameGrid[x][y] += 1;
        }
    }

}
