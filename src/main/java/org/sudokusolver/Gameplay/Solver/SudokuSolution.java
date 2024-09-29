package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.Sudoku.Sudoku;

public record SudokuSolution(Sudoku sudoku, DifficultyLevel difficultyLevel) {
}
