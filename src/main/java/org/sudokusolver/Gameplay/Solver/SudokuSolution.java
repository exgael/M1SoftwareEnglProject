package org.sudokusolver.Gameplay.Solver;

import org.sudokusolver.Gameplay.SudokuBoard;

public record SudokuSolution(SudokuBoard sudokuBoard, DifficultyLevel difficultyLevel) {
}
