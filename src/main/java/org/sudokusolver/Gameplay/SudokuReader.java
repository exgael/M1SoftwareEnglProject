package org.sudokusolver.Gameplay;

@FunctionalInterface
public interface SudokuReader {
    int[] readGridFrom(String str) throws RuntimeException;
}
