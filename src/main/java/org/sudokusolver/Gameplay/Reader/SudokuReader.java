package org.sudokusolver.Gameplay.Reader;

@FunctionalInterface
public interface SudokuReader {
    int[] readGridFrom(String str) throws RuntimeException;
}
