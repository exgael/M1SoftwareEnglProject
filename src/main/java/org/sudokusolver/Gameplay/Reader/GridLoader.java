package org.sudokusolver.Gameplay.Reader;

import java.util.logging.Logger;

public class GridLoader {
    private static final Logger logger = Logger.getLogger(GridLoader.class.getName());

    public int[] loadNewSudoku(String str, boolean isPath) throws RuntimeException {
        logger.info("Loading new grid...");
        SudokuReader loader = ReaderFactory.getReader(isPath);
        return loader.readGridFrom(str);
    }
}
