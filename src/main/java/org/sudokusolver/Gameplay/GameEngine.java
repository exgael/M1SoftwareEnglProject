package org.sudokusolver.Gameplay;

import org.sudokusolver.Gameplay.Sudoku.Sudoku;
import org.sudokusolver.Utils.Reader.GridLoader;

import java.util.logging.Logger;

public class GameEngine {

    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());
    // Sudoku & solver
    private final Sudoku sudoku;
    private final SudokuSolver solver;
    // Storage
    private final GridLoader gridLoader;
    // GUI
    private GameInterface gameInterface;

    private int[] currentGrid;

    public GameEngine(GridLoader gridLoader, SudokuSolver solver, Sudoku sudoku) {
        this.gridLoader = gridLoader;
        this.solver = solver;
        this.sudoku = sudoku;
    }

    public void setGameInterface(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
        sudoku.addObserver(gameInterface);
    }

    public void loadGridFromPath(String filePath) {
        load(filePath, true);
    }

    public void loadGridFromString(String gridString) {
        load(gridString, false);
    }

    private void load(String str, boolean isPath) {
        currentGrid = gridLoader.loadNewSudoku(str, isPath);
        sudoku.load(currentGrid);
    }

    public void play() {
        logger.info("Solver trying to solve sudoku");
        int level = solver.trySolveSudoku(sudoku);

        if (sudoku.isSolved()) {
            logger.info("Sudoku solved");
            finishGame(level);
        } else {
            logger.info("Solver couldn't make progress, requesting user input");
            gameInterface.onRequestUserInput();
        }
    }

    private void finishGame(int level) {
        gameInterface.onSudokuFinished(level);
    }

    public void receiveUserMove(UserMove move) {
        logger.info("Received user move: " + move);
        try {
            sudoku.setValue(move.row(), move.col(), move.value());
            play(); // continue playing
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            gameInterface.onInvalidMove(move.value(), move.row(), move.col());
            sudoku.load(currentGrid);
        }
    }

    public void resetSudoku() {
        if (currentGrid != null) {
            sudoku.load(currentGrid);
            logger.info("Sudoku grid reset to initial state.");
        } else {
            logger.warning("No initial grid loaded to reset.");
        }
    }
}
