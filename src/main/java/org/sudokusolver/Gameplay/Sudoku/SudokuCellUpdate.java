package org.sudokusolver.Gameplay.Sudoku;

import java.util.Set;

public record SudokuCellUpdate(int row, int col, int value, Set<Integer> candidates) {
}