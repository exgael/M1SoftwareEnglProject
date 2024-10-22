package org.sudokusolver.Gameplay;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;
import org.sudokusolver.Utils.Subject;

public interface Sudoku extends Modifiable, Solvable, Loadable, Subject<SudokuUpdate> {
    SudokuCell getElement(int row, int col);

    int getBoardSize();

    int getSubgridSize();
}
