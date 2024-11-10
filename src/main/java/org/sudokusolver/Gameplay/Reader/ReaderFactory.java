package org.sudokusolver.Gameplay.Reader;

public class ReaderFactory {
    public static SudokuReader getReader(boolean isPath) {
        if (isPath) {
            return new SudokuFileReader();
        } else {
            return new SudokuStringReader();
        }
    }
}
