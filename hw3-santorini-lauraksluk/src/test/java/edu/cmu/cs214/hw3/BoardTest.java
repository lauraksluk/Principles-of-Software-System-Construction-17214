package edu.cmu.cs214.hw3;

import static org.junit.Assert.*;

import edu.cmu.cs214.hw3.core.Board;
import edu.cmu.cs214.hw3.core.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {
    private Board b;
    private int[][] grid;
    private List<List<Worker>> workers;

    @Before
    public void setUp() {
        b = new Board();
        grid = b.getGameGrid();
        workers = b.getWorkers();
    }

    @Test
    public void testInitializeWorkers() {
        b.initializeWorkers(0, 0, 3, 4);
        int[] pos = {3, 4};
        assertArrayEquals(workers.get(0).get(0).getPosition(), pos);
    }

    @Test
    public void testAddBlock() {
        grid[2][3] = 4;
        b.addBlock(2, 3);
        assertEquals(grid[2][3], 4);
        b.addBlock(0, 0);
        assertEquals(grid[0][0], 1);

    }

}
