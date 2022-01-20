package edu.cmu.cs214.hw3;

import static org.junit.Assert.*;

import edu.cmu.cs214.hw3.core.Board;
import edu.cmu.cs214.hw3.core.GameSystem;
import org.junit.Before;
import org.junit.Test;

public class GameSystemWinTest {
    private GameSystem game;
    private int[][] grid;
    private Board b;

    @Before
    public void setUp() {
        game = new GameSystem();
        b = game.getGameBoard();
        grid = game.getGameBoard().getGameGrid();
    }

    @Test
    public void testInitialPlaceWorkers() {
        assertEquals(game.getCurrPlayer(), 0);
        game.placeWorkers(4, 0, 1, 1);
        int[] pos11 = {4, 0};
        int[] pos12 = {1, 1};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(0).getPosition(), pos11);
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(1).getPosition(), pos12);
        assertEquals(game.getCurrPlayer(), 1);
        game.placeWorkers(3, 4, 1, 3);
        int[] pos21 = {3, 4};
        int[] pos22 = {1, 3};
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(0).getPosition(), pos21);
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(1).getPosition(), pos22);
        assertEquals(game.getCurrPlayer(), 0);
    }

    @Test
    public void testMove1SameLevel() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertEquals(game.getCurrPlayer(), 0);
        assertFalse(game.move(0, 1, 0));
        assertEquals(game.getCurrPlayer(), 0);
        assertTrue(game.move(0, 0, 1));
        int[] pos1 = {4, 1};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(0).getPosition(), pos1);
        assertEquals(game.getCurrWorker(), 0);
        assertEquals(game.getCurrPlayer(), 0);
    }

    @Test
    public void testBuild11() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertEquals(game.getCurrWorker(), 0);
        assertEquals(game.getCurrPlayer(), 0);
        assertTrue(game.build(3, 1));
        assertEquals(grid[3][1], 1);
        assertEquals(game.getCurrPlayer(), 1);
    }

    @Test
    public void testMove2SameLevel() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertEquals(game.getCurrPlayer(), 1);
        assertTrue(game.move(0, -1, 0));
        int[] pos1 = {2, 4};
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(0).getPosition(), pos1);
        assertEquals(game.getCurrPlayer(), 1);
        assertEquals(game.getCurrWorker(), 0);
    }

    @Test
    public void testBuild21() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertEquals(game.getCurrPlayer(), 1);
        assertEquals(game.getCurrWorker(), 0);
        assertTrue(game.build(2, 3));
        assertEquals(grid[2][3], 1);
        assertEquals(game.getCurrPlayer(), 0);
    }

    @Test
    public void testMove12SameLevel() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertEquals(game.getCurrPlayer(), 0);
        assertTrue(game.move(1, 1, -1));
        int[] pos1 = {2, 0};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(1).getPosition(), pos1);
        assertEquals(game.getCurrPlayer(), 0);
        assertEquals(game.getCurrWorker(), 1);
    }

    @Test
    public void testBuild12() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertTrue(game.move(1, 1, -1));
        assertTrue(game.build(2, 1));
        assertEquals(grid[2][1], 1);
        assertEquals(game.getCurrPlayer(), 1);
    }

    @Test
    public void testMove2UpBuild() {
        game.placeWorkers(4, 0, 1, 1);
        game.placeWorkers(3, 4, 1, 3);
        assertTrue(game.move(0, 0, 1));
        assertTrue(game.build(3, 1));
        assertTrue(game.move(0, -1, 0));
        assertTrue(game.build(2, 3));
        assertTrue(game.move(1, 1, -1));
        assertTrue(game.build(2, 1));
        assertEquals(game.getCurrPlayer(), 1);
        assertTrue(game.move(1, 1, 0));
        assertEquals(game.getCurrPlayer(), 1);
        assertEquals(game.getCurrWorker(), 1);
        assertTrue(game.build(3, 3));
        assertEquals(grid[3][3], 1);
        assertEquals(game.getCurrPlayer(), 0);
    }

    @Test
    public void testMove1UpBuild() {
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
        assertEquals(grid[2][1], 2);
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
        int[] pos1 = {3, 4};
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(0).getPosition(), pos1);
        assertTrue(game.build(3, 3));
        assertEquals(grid[3][3], 2);
        assertEquals(game.getCurrPlayer(), 0);
    }

    @Test
    public void testInvalidMoveUp() {
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
        assertEquals(game.getCurrPlayer(), 0);
        assertFalse(game.move(1, 0, 1));
        assertTrue(game.move(0, -1, 0));
        int[] pos1 = {2, 1};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(0).getPosition(), pos1);
        assertEquals(grid[2][1], 2);
        assertEquals(game.getCurrWorker(), 0);
        assertTrue(game.build(3, 1));
        assertEquals(grid[3][1], 2);
    }

    @Test
    public void testInvalidMoveBuild22() {
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
        assertEquals(game.getCurrPlayer(), 1);
        assertFalse(game.move(0, 0, 1));
        assertTrue(game.move(0, 1, -1));
        assertTrue(game.build(3, 3));
        assertEquals(grid[3][3], 3);
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
        assertEquals(game.getCurrPlayer(), 0);
        assertFalse(game.move(1, 0, -1));
        assertTrue(game.move(1, 1, 0));
        assertTrue(game.build(3, 1));
        assertEquals(grid[3][1], 3);
        assertEquals(game.getCurrPlayer(), 1);
        assertFalse(game.getWin());
        assertFalse(game.getLose());
    }

    @Test
    public void testMoveBuild22() {
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
        assertFalse(game.move(0, -1, 0));
        assertFalse(game.move(1, 1, 0));
        assertTrue(game.move(0, -1, -1));
        int[] pos1 = {3, 2};
        assertArrayEquals(game.getGameBoard().getWorkers().get(1).get(0).getPosition(), pos1);
        assertTrue(game.build(3, 3));
        assertEquals(grid[3][3], 4);
    }

    @Test
    public void testWin() {
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
        assertTrue(game.build(3, 3));
        assertTrue(game.move(0, 1, 0));
        int[] pos1 = {3, 1};
        assertArrayEquals(game.getGameBoard().getWorkers().get(0).get(0).getPosition(), pos1);
        assertTrue(game.getWin());
    }

}
