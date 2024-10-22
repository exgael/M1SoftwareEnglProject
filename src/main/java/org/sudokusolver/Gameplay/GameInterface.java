package org.sudokusolver.Gameplay;

import org.sudokusolver.Strategy.Sudoku.SudokuCellUpdate;
import org.sudokusolver.Utils.Observer;

public interface GameInterface extends Observer<SudokuCellUpdate> {
    void onRequestUserInput();
    void onSudokuFinished(int level);
    void onInvalidMove(int value, int row, int col);
}