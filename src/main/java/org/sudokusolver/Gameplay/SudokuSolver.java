package org.sudokusolver.Gameplay;


public interface SudokuSolver {
    int trySolveSudoku(Solvable sudoku);
    void reset();
}
