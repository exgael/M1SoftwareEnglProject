package org.sudokusolver.Strategy.Sudoku;

import java.util.Set;

public record SudokuCellUpdate(int row, int col, int value, Set<Integer> candidates) {
}