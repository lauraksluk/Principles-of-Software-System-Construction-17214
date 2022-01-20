package edu.cmu.cs214.hw3.core;

import java.util.List;

public class GameLogic {

    public boolean playMove(Board gBoard, int playerId, int workerId, int delX, int delY) {
        Worker currW = gBoard.getWorker(playerId, workerId);
        int[] currPos = currW.getPosition();
        if (isValidMove(gBoard, playerId, workerId, delX, delY)) {
            currW.setPosition(currPos[0] + delX, currPos[1] + delY);
            return true;
        }
        return false;
    }

    public final boolean isValidMove(Board gBoard, int playerId, int workerId, int delX, int delY) {
        Worker currW = gBoard.getWorker(playerId, workerId);
        int[] currPos = currW.getPosition();
        if (!inBounds(gBoard,currPos[0] + delX, currPos[1] + delY)) {
            return false;
        }
        if (!isAdjacent(currPos[0], currPos[1], currPos[0] + delX, currPos[1] + delY)) {
            return false;
        }
        int currH = gBoard.getCurrentHeight(currPos[0], currPos[1]);
        int newH = gBoard.getCurrentHeight(currPos[0] + delX, currPos[1] + delY);
        if (newH - currH > 1) {
            return false;
        }
        if (newH == gBoard.MAX_HEIGHT) {
            return false;
        }
        if (isOccupied(gBoard, playerId, workerId, currPos[0] + delX, currPos[1] + delY)) {
            return false;
        }
        return true;
    }

    public boolean winGame(Board gBoard) {
        if (winDefault(gBoard)) {
            return true;
        }
        return false;
        //winAlt()
    }

    //public abstract boolean winAlt();

    private final boolean winDefault(Board gBoard) {
        List<List<Worker>> workers = gBoard.getWorkers();
        for (List<Worker> w1 : workers) {
            for (Worker w : w1) {
                int[] pos = w.getPosition();
                int x = pos[0];
                int y = pos[1];
                if (gBoard.getCurrentHeight(x, y) == gBoard.WIN_HEIGHT) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean buildBlock(Board gBoard, int playerId, int workerId, int x, int y) {
        if (isValidBuild(gBoard, playerId, workerId, x, y)) {
            gBoard.addBlock(x, y);
            return true;
    //DEMETER EXTRA TURN
        }
        return false;
    }

    public final boolean isValidBuild(Board gBoard, int playerId, int workerId, int x, int y) {
        Worker currentW = gBoard.getWorkers().get(playerId).get(workerId);
        int[] workerPosition = currentW.getPosition();
        if (!isAdjacent(workerPosition[0], workerPosition[1], x, y)) {
            return false;
        }
        if (!inBounds(gBoard, x, y)) {
            return false;
        }
        if (isOccupied(gBoard, playerId, workerId, x, y)) {
            return false;
        }
        int currH = gBoard.getCurrentHeight(x, y);
        if (currH == gBoard.MAX_HEIGHT) {
            return false;
        }
        return 0 <= currH && currH < gBoard.MAX_HEIGHT;
    }

    public boolean isOccupied(Board b, int pId, int wId, int x, int y) {
        List<List<Worker>> workers = b.getWorkers();
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

    private boolean isAdjacent(int initialX, int initialY, int finalX, int finalY) {
        if (initialX == finalX && initialY == finalY) {
            return false;
        }
        int delX = finalX - initialX;
        int delY = finalY - initialY;
        return (-1 <= delX && delX <= 1 && -1 <= delY && delY <= 1);
    }

    private boolean inBounds(Board gboard, int x1, int y1) {
        return 0 <= x1 && x1 < gboard.BOARD_WIDTH && 0 <= y1 && y1 < gboard.BOARD_HEIGHT;
    }

    /**
     * Method to determine if a player couldn't complete a move, thereby losing the game.
     * @param playerId current player
     * @return true, if player loses, false otherwise
     */
    public boolean loseGameMove(Board gBoard, int playerId) {
        int[] allDirections = {0, -1, 1};
        List<Worker> workers = gBoard.getWorkers().get(playerId);
        for (Worker w : workers) {
            int[] position = w.getPosition();
            int x = position[0];
            int y = position[1];
            for (int i = 0; i < allDirections.length; i++) {
                for (int j = 0; j < allDirections.length; j++) {
                    if (i != 0 && j != 0) {
                        if (isValidMove(gBoard, playerId, 0, allDirections[i], allDirections[j])) {
                            return false;
                        }
                        if (isValidMove(gBoard, playerId, 1, allDirections[i], allDirections[j])) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method to determine if a player couldn't complete a build, thereby losing the game.
     * @param playerId current player
     * @param workerId current worker
     * @return true, if player loses, false otherwise
     */
    public boolean loseGameBuild(Board b, int playerId, int workerId) {
        int[] allDirections = {-1, 0, 1};
        Worker currentW = b.getWorkers().get(playerId).get(workerId);
        int[] position = currentW.getPosition();
        int x = position[0];
        int y = position[1];
        for (int i = 0; i < allDirections.length; i++) {
            for (int j = 0; j < allDirections.length; j++) {
                if (i != 0 && j != 0) {
                    if (isValidBuild(b, playerId, workerId, x + allDirections[i], y + allDirections[j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
