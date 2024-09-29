package org.sudokusolver.Gameplay.Sudoku;

import org.sudokusolver.Utils.Inspectable;

public interface Sudoku extends Inspectable {
    int getValue(int row, int col);

    void setValue(int row, int col, int value);

    boolean isSolved();

    SudokuCell getElement(int row, int col);

    int getBoardSize();

    int getSubgridSize();
}
