package game;

import java.util.ArrayList;
import java.util.List;


enum Player {
    PLAYER0(0), PLAYER1(1);

    final int value;

    Player(int value) {
        this.value = value;
    }
}

public class Game {
    private final Board board;
    private final Player player;
    private final List<Game> history;

    public Game() {
        this(new Board(), Player.PLAYER0);
    }

    public Game(Board board, Player nextPlayer) {
        this(board, nextPlayer, List.of());
    }

    public Game(Board board, Player nextPlayer, List<Game> history) {
        this.board = board;
        this.player = nextPlayer;
        this.history = history;
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Game play(int x, int y) {
        if (this.board.getCell(x,y) != null) return this;
        if (this.getWinner() != null) return this;
        List<Game> newHistory = new ArrayList<>(this.history);
        newHistory.add(this);
        Player nextPlayer = this.player == Player.PLAYER0 ? Player.PLAYER1 : Player.PLAYER0;
        return new Game(this.board.updateCell(x, y, this.player), nextPlayer, newHistory);
    }

    public Player getWinner() {
        for (int row = 0; row < 3; row++)
            if (board.getCell(row, 0) != null && board.getCell(row, 0) == board.getCell(row, 1)
                    && board.getCell(row, 1) == board.getCell(row, 2)) return board.getCell(row, 0);
        for (int col = 0; col < 3; col++)
            if (board.getCell(0, col) != null && board.getCell(0, col) == board.getCell(1, col)
                    && board.getCell(0, col) == board.getCell(2, col)) return board.getCell(0, col);
        if (board.getCell(1, 1) != null && board.getCell(0, 0) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 2)) return board.getCell(1, 1);
        if (board.getCell(1, 1) != null && board.getCell(0, 2) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 0)) return board.getCell(1, 1);
        return null;
    }
}
