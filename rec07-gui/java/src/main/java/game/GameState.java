package game;

import java.util.Arrays;

public class GameState {

    private final Cell[] cells;

    private GameState(Cell[] cells) {
        this.cells = cells;
    }

    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        return new GameState(cells);
    }


    public Cell[] getCells() {
        return this.cells;
    }

    @Override
    public String toString() {
        return "GameState[" +
                "cells=" + Arrays.toString(this.cells) + ']';
    }

    private static Cell[] getCells(Game game) {
        Cell cells[] = new Cell[9];
        Board board = game.getBoard();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                String text = "";
                String link = "";
                String clazz = "";
                Player player = board.getCell(x, y);
                if (player == Player.PLAYER0) text = "X";
                else if (player == Player.PLAYER1) text = "O";
                else if (player == null) {
                    clazz = "playable";
                    link = "/play?x=" + x + "&y=" + y;
                }
                cells[3 * y + x] = new Cell(text, clazz, link);
            }
        }
        return cells;
    }
}

class Cell {
    private final String text;
    private final String clazz;
    private final String link;

    Cell(String text, String clazz, String link) {
        this.text = text;
        this.clazz = clazz;
        this.link = link;
    }

    public String getText() {
        return this.text;
    }

    public String getClazz() {
        return this.clazz;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public String toString() {
        return "Cell[" +
                "text=" + this.text + ", " +
                "clazz=" + this.clazz + ", " +
                "link=" + this.link + ']';
    }
}