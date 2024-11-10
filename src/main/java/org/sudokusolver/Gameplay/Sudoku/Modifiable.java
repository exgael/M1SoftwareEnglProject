package org.sudokusolver.Gameplay.Sudoku;

@FunctionalInterface
public interface Modifiable {
    void setValue(int row, int col, int value);
}
