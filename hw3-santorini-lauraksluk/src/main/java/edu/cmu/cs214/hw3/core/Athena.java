package edu.cmu.cs214.hw3.core;

public class Athena extends GameLogic {
    private boolean canOpponentMoveUp;

    @Override
    public boolean playMove(Board gBoard, int playerId, int workerId, int delX, int delY) {
        //.......//
        return super.playMove(gBoard, playerId, workerId, delX, delY);
    }

    public boolean canOpponentUp() {
        //.......//
        return true;
    }
}
