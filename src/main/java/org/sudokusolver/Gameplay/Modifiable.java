package org.sudokusolver.Gameplay;

@FunctionalInterface
public interface Modifiable {
    void setValue(int row, int col, int value);
}
