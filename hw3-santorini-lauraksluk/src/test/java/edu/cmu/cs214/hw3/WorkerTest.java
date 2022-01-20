package edu.cmu.cs214.hw3;

import static org.junit.Assert.*;

import edu.cmu.cs214.hw3.core.Worker;
import org.junit.Before;
import org.junit.Test;

public class WorkerTest {
    private Worker w;

    @Before
    public void setUp() {
        w = new Worker(0);
    }

    @Test
    public void testWorkerPosition() {
        assertEquals(w.getWorkerId(), 0);
        int[] initPos = {0, 0};
        assertArrayEquals(w.getPosition(), initPos);
        w.setPosition(1, 1);
        int[] pos = {1, 1};
        assertArrayEquals(w.getPosition(), pos);
    }

    @Test
    public void testWorkerEqual() {
        w.setPosition(2, 2);
        Worker w1 = new Worker(0);
        w1.setPosition(2, 2);
        assertTrue(w.equals(w1));
    }
}
