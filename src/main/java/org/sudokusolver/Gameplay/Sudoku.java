package org.sudokusolver.Gameplay;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;
import org.sudokusolver.Utils.Subject;

public interface Sudoku extends Subject<SudokuUpdate> {
    int getValue(int row, int col);

    void setValue(int row, int col, int value);

    boolean isSolved();

    SudokuCell getElement(int row, int col);

    int getBoardSize();

    int getSubgridSize();

    void load(int[] grid);
}
