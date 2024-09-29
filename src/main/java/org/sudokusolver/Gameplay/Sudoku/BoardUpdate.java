package org.sudokusolver.Gameplay.Sudoku;

public record BoardUpdate<T>(int row, int col, T oldValue, T newValue) {
}