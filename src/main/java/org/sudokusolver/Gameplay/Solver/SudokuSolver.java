package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.Solver.SudokuSolution;
import org.sudokusolver.Gameplay.Sudoku.Sudoku;

public interface SudokuSolver {
    SudokuSolution trySolveSudoku(Sudoku sudoku);
}
