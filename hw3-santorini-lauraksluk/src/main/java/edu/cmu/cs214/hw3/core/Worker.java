package edu.cmu.cs214.hw3.core;

import java.util.Arrays;

/**
 * Worker class that represents each player's workers.
 */
public class Worker {
    private int workerId;
    private int[] position;

    /**
     * Constructor to create a worker.
     * @param id worker id
     */
    public Worker(int id) {
        workerId = id;
        position = new int[2];
    }

    /**
     * Method to get the worker's id.
     * @return worker id
     */
    public int getWorkerId() {
        return workerId;
    }

    /**
     * Method to get the position of the worker.
     * @return [x, y] coordinate value
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * Method to set the position of the worker.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

    /**
     * Method that overrides Java equals(); workers are considered equal if they have the same
     * worker id, and position.
     * @param o object to compare
     * @return true, if o is equal to worker, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        Worker w = (Worker) o;
        int[] wPos = w.getPosition();
        return w.getWorkerId() == workerId && wPos[0] == position[0]
                && wPos[1] == position[1];
    }

    /**
     * Method that overrides Java hashCode(); for equality override
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(position);
    }

}
