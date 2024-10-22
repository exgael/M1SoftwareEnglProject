package org.sudokusolver.Gameplay;

@FunctionalInterface
public interface SudokuSolver {
    int trySolveSudoku(Solvable sudoku);
}
