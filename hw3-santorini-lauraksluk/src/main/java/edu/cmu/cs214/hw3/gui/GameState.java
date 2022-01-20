package edu.cmu.cs214.hw3.gui;

import edu.cmu.cs214.hw3.core.Board;
import edu.cmu.cs214.hw3.core.GameSystem;

import java.util.Arrays;

public class GameState {
    private final Cell[] cells;

    private GameState(Cell[] c) {
        this.cells = c;
    }

    public Cell[] getCells() {
        return this.cells;
    }

    public static GameState forGame(GameSystem game) {
        Cell[] cells = getCells(game);
        return new GameState(cells);
    }

    @Override
    public String toString() {
        return "GameState[" +
                "cells=" + Arrays.toString(this.cells) + ']';
    }

    private static Cell[] getCells(GameSystem game) {
        Cell cells[] = new Cell[25];
        Board board = game.getGameBoard();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                String text = "";
                cells[5 * y + x] = new Cell(text);
            }
        }
        return cells;
    }
}

class Cell {
    private final String text;

    Cell(String t) {
        this.text = t;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Cell[" + "text=" + this.text + ']';
    }
}