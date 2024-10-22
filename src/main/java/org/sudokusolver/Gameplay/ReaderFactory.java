package org.sudokusolver.Gameplay;

import org.sudokusolver.Utils.File.SudokuStringReader;
import org.sudokusolver.Utils.File.SudokuFileReader;

public class ReaderFactory {
    public static SudokuReader getReader(boolean isPath) {
        if (isPath) {
            return new SudokuFileReader();
        } else {
            return new SudokuStringReader();
        }
    }
}
