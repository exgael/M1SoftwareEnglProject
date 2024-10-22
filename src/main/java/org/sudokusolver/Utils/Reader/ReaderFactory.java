package org.sudokusolver.Utils.Reader;

import org.sudokusolver.Gameplay.SudokuReader;

public class ReaderFactory {
    public static SudokuReader getReader(boolean isPath) {
        if (isPath) {
            return new SudokuFileReader();
        } else {
            return new SudokuStringReader();
        }
    }
}
