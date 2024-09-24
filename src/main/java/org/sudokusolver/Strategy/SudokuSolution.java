package org.sudokusolver.Strategy;

import org.sudokusolver.Gameplay.SudokuBoard;

public record SudokuSolution(SudokuBoard sudokuBoard, int difficulty) {
}
