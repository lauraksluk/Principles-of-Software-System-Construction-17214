package edu.cmu.cs214.hw3.core;

import java.util.List;

public class Minotaur extends GameLogic {

    @Override
    public boolean isOccupied(Board b, int pId, int wId, int x, int y) {
        Worker currW = b.getWorker(pId, wId);
        int[] currPos = currW.getPosition();
        int delX = x - currPos[0];
        int delY = y - currPos[1];
        int[] otherPos = b.getWorker(pId, (wId + 1) % 2).getPosition();
        if (x == otherPos[0] && y == otherPos[1]) {
            return true;
        }
        List<List<Worker>> workers = b.getWorkers();
        for (List<Worker> w1 : workers) {
            for (Worker w : w1) {
                int[] wPosition = w.getPosition();
                if (wPosition[0] == x && wPosition[1] == y) {
                    if (canEvict(b, wPosition[0] + delX, wPosition[1]+ delY)) {
                        w.setPosition(wPosition[0] + delX, wPosition[1] + delY);
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canEvict(Board b, int x, int y) {
        List<List<Worker>> workers = b.getWorkers();
        for (List<Worker> w1 : workers) {
            for (Worker w : w1) {
                int[] wPosition = w.getPosition();
                if (wPosition[0] == x && wPosition[1] == y) {
                    return false;
                }
            }
        }
        return true;
    }
}
