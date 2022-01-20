package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.core.Board;
import edu.cmu.cs214.hw3.core.GameLogic;
import edu.cmu.cs214.hw3.core.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameLogicTest {
    private GameLogic gl;
    private Board b;
    private int[][] grid;
    private List<List<Worker>> workers;

    @Before
    public void setUp() {
        b = new Board();
        grid = b.getGameGrid();
        workers = b.getWorkers();
        gl = new GameLogic();
    }

    @Test
    public void testWin() {
        grid[3][3] = 3;
        b.getWorkers().get(0).get(0).setPosition(3, 3);
        assertTrue(gl.winGame(b));
    }

    @Test
    public void testLoseMove() {
        grid[1][0] = 2;
        grid[1][1] = 2;
        grid[2][1] = 2;
        grid[3][1] = 2;
        grid[4][0] = 2;
        grid[4][1] = 2;
        workers.get(0).get(0).setPosition(2, 0);
        workers.get(0).get(1).setPosition(3, 0);
        assertTrue(gl.loseGameMove(b,0));
    }

    @Test
    public void testLoseBuild() {
        workers.get(0).get(0).setPosition(2, 0);
        workers.get(0).get(1).setPosition(3, 0);
        workers.get(1).get(0).setPosition(3, 1);
        workers.get(1).get(1).setPosition(1, 1);
        grid[1][0] = 4;
        grid[2][1] = 4;
        grid[4][0] = 4;
        grid[4][1] = 4;
        assertTrue(gl.loseGameBuild(b, 0, 0));
    }

    @Test
    public void testMove() {
        b.initializeWorkers(0, 0, 3, 4);
        assertFalse(gl.buildBlock(b, 0, 0, 2, 3));
        assertTrue(gl.playMove(b, 0, 0, 1, -1));
        assertFalse(gl.buildBlock(b, 0, 0, 3, 3));
    }

    @Test
    public void testBuild() {
        b.initializeWorkers(0, 0, 3, 4);
        gl.playMove(b, 0, 0, 1, -1);
        assertFalse(gl.buildBlock(b, 0, 0, 3, 3));
    }

}
