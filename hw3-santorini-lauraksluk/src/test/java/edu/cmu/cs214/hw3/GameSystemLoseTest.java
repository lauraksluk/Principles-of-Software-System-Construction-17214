package edu.cmu.cs214.hw3;

import edu.cmu.cs214.hw3.core.Board;
import edu.cmu.cs214.hw3.core.GameSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class GameSystemLoseTest {
    private GameSystem game;
    private Board b;
    private int[][] grid;

    @Before
    public void setUp() {
        game = new GameSystem();
        b = game.getGameBoard();
        grid = game.getGameBoard().getGameGrid();
    }

    @Test
    public void testMoveBuild1() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertTrue(game.move(1, 1, -1));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(0, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, 1, -1));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 1));
        assertEquals(grid[3][1], 3);
        assertEquals(game.getCurrPlayer(), 1);
    }

    @Test
    public void testMoveBuild2() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertTrue(game.move(1, 1, -1));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(0, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, 1, -1));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, -1));
        assertTrue(game.build(3, 1));
        assertEquals(grid[3][1], 4);
        assertEquals(game.getCurrPlayer(), 0);
        assertTrue(game.move(0, 0, -1));
        assertTrue(game.build(1, 0));
        assertTrue(game.move(0, 1, 0));
        int[] pos1 = {4, 2};
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(0).getPosition(), pos1);
        assertTrue(game.build(4, 1));
    }

    @Test
    public void testMoveBuild3() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertTrue(game.move(1, 1, -1));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 1));
        assertTrue(game.move(0, 1, 0));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, 1, -1));
        assertTrue(game.build(3, 3));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, -1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, 0, -1));
        assertTrue(game.build(1, 0));
        assertTrue(game.move(0, 1, 0));
        assertTrue(game.build(4, 1));
        assertTrue(game.move(1, 1, 0));
        int[] pos1 = {4, 0};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(1).getPosition(), pos1);
        assertTrue(game.build(4, 1));
        assertEquals(grid[4][1], 2);
    }

    @Test
    public void testLose() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        game.move(1, 1, -1);
        game.move(0, -1, 0);
        game.build(3, 1);
        int[] pos1 = {2, 0};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(1).getPosition(), pos1);
        int[] pos2 = {3, 0};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(0).getPosition(), pos2);
        grid[1][0] = 2;
        grid[1][1] = 2;
        grid[2][1] = 2;
        grid[3][1] = 2;
        grid[4][0] = 3;
        grid[4][1] = 2;
        assertEquals(game.getCurrPlayer(), 1);
        game.move(0, 0, -1);
        game.build(3, 2);
        assertFalse(game.move(0, 0, 1));
        assertTrue(game.getLose());
    }

}
