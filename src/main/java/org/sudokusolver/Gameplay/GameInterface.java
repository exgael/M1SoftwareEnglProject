package org.sudokusolver.Gameplay;

public interface GameInterface {
    void onRequestUserInput();
    void onSudokuFinished(int level);
    void onInvalidMove(int value, int row, int col);
}
