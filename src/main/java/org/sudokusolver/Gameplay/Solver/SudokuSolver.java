package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.SudokuBoard;

public interface SudokuSolver {
    SudokuSolution trySolveSudoku(SudokuBoard sudoku);
}
