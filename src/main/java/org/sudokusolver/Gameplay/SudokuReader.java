package org.sudokusolver.Gameplay;

public interface SudokuReader {
    public int[] readGridFrom(String str) throws RuntimeException;
}
