package org.sudokusolver.Strategy.Sudoku;

import java.util.Set;

public record SudokuUpdate(int row, int col, int value, Set<Integer> candidates) {
}