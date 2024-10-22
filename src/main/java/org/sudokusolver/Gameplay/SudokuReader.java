package org.sudokusolver.Gameplay;

public interface SudokuReader {
    int[] readGridFrom(String str) throws RuntimeException;
}
