package org.sudokusolver.Gameplay;

import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;
import org.sudokusolver.Utils.Observer;

public interface GameInterface extends Observer<SudokuUpdate> {
    void onRequestUserInput();
    void onSudokuFinished(int level);
    void onInvalidMove(int value, int row, int col);
}