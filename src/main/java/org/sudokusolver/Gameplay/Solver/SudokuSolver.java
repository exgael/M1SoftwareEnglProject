package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.Sudoku.SudokuBoard;

public interface SudokuSolver {
    SudokuSolution trySolveSudoku(SudokuBoard sudoku);
}
