package edu.cmu.cs214.hw3.core;

import java.util.ArrayList;
import java.util.List;


/**
 * Main class that controls all aspects of the game; acts as a controller.
 */
public class GameSystem {
    private int numPlayers;
    private Board gameBoard;
    private List<Integer> players;
    private GameLogic logic1;
    private GameLogic logic2;
    private int currPlayer;
    private int currWorker;
    private boolean win;
    private boolean lose;

    /**
     * Constructor for game controller. Initializes environment.
     */
    public GameSystem() {
        numPlayers = 2;
        gameBoard = new Board();
        players = new ArrayList<>(numPlayers);
        players.add(0);
        players.add(1);
        currPlayer = 0;
        win = false;
        lose = false;
        logic1 = new GameLogic();
        logic2 = new GameLogic();
    }

    /**
     * Method to place both workers in the beginning of the game. The inputted (x1,y1) and (x2,y2)
     * must be in bounds; (0 <= x1,y1,x2,y2 < 5).
     * @param x1 x-coordinate of worker1
     * @param y1 y-coordinate of worker1
     * @param x2 x-coordinate of worker2
     * @param y2 y-coordinate of worker2
     */
    public void placeWorkers(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            throw new IllegalArgumentException("both positions can't be identical");
        }
        try {
            gameBoard.initializeWorkers(currPlayer, 0, x1, y1);
            gameBoard.initializeWorkers(currPlayer, 1, x2, y2);
            updatePlayer();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * Method for player to move. The inputted (delX, delY) must be within bounds
     * (0 <= delX, delY < 5).
     * @param workerId the worker to move
     * @param delX the amount to move in x-direction
     * @param delY the amount to move in y-direction
     * @return true, if move was successful, false otherwise
     */
    public boolean move(int workerId, int delX, int delY) {
        if (currPlayer == 0) {
            if (logic1.playMove(gameBoard, currPlayer, workerId, delX, delY)) {
                currWorker = workerId;
                if (logic1.winGame(gameBoard)) {
                    win = true;
                }
                return true;
            }
            if (logic1.loseGameMove(gameBoard, 0)) {
                lose = true;
            }
            return false;
        }
        else if (currPlayer == 1) {
            if (logic2.playMove(gameBoard, currPlayer, workerId, delX, delY)) {
                currWorker = workerId;
                if (logic2.winGame(gameBoard)) {
                    win = true;
                }
                return true;
            }
            if (logic2.loseGameMove(gameBoard, 1)) {
                lose = true;
            }
        }

        return false;
    }

    /**
     * Method for player to build (block/dome), after their move. The inputted (x, y) must
     * be within bounds (0 <= x, y < 5).
     * @param x x-coordinate to build
     * @param y y-coordinate to build
     * @return true, if build was successful, false otherwise
     */
    public boolean build(int x, int y) {
        if (currPlayer == 0) {
            if (logic1.buildBlock(gameBoard, currPlayer, currWorker, x, y)) {
                updatePlayer();
                return true;
            }
            if (logic1.loseGameBuild(gameBoard, currPlayer, currWorker)) {
                lose = true;
            }
        }
        else if (currPlayer == 1) {
            if (logic2.buildBlock(gameBoard, currPlayer, currWorker, x, y)) {
                updatePlayer();
                return true;
            }
            if (logic2.loseGameBuild(gameBoard, currPlayer, currWorker)) {
                lose = true;
            }
        }
        return false;
    }

    /**
     * Method to get the number of players in the game.
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Method to return the list of players in the game.
     * @return list of players
     */
    public List<Integer> getPlayers() {
        return players;
    }

    /**
     * Method to update the current player.
     */
    public void updatePlayer() {
        currPlayer = (currPlayer + 1) % numPlayers;
    }

    /**
     * Method to get (index of) the current player.
     * @return current player
     */
    public int getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Method to get (index of) the current worker.
     * @return current worker
     */
    public int getCurrWorker() {
        return currWorker;
    }

    /**
     * Method to get the game board/grid object.
     * @return gameboard
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     * Method to get win status.
     * @return true, if won, false otherwise
     */
    public boolean getWin() {
        return win;
    }

    /**
     * Method to get lose status.
     * @return true, if lost, false otherwise
     */
    public boolean getLose() {
        return lose;
    }

}
