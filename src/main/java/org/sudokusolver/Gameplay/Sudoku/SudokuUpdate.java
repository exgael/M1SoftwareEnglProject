package org.sudokusolver.Gameplay.Sudoku;

import java.util.Set;

public record SudokuUpdate(int row, int col, int value, Set<Integer> candidates) {
}