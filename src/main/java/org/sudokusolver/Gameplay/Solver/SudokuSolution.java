package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.Sudoku.SudokuBoard;

public record SudokuSolution(SudokuBoard sudokuBoard, DifficultyLevel difficultyLevel) {
}
