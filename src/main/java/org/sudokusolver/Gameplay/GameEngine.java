package org.sudokusolver.Gameplay;

import org.sudokusolver.Utils.File.GridLoader;

import java.util.logging.Logger;

public class GameEngine {

    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());

    // GUI
    private GameInterface gameInterface;

    // Sudoku & solver
    private final Sudoku sudoku;
    private final SudokuSolver solver;

    // Storage
    private final GridLoader gridLoader;

    public GameEngine(GridLoader gridLoader, SudokuSolver solver, Sudoku sudoku) {
        this.gridLoader = gridLoader;
        this.solver = solver;
        this.sudoku = sudoku;
    }

    public void setGameInterface(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
        registerToSudokuUpdate(gameInterface);
    }

    private void registerToSudokuUpdate(GameInterface gameInterface) {
        for (int row = 0; row < sudoku.getBoardSize(); row++) {
            for (int col = 0; col < sudoku.getBoardSize(); col++) {
                sudoku.getElement(row, col).addObserver(gameInterface);
            }
        }
    }

    public void loadGridFromPath(String filePath) {
        var grid = gridLoader.loadNewSudoku(filePath, true);
        sudoku.load(grid);
    }

    public void loadGridFromString(String gridString) {
        var grid = gridLoader.loadNewSudoku(gridString, false);
        sudoku.load(grid);
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
        System.out.println(sudoku.debugDescription());
        gameInterface.onSudokuFinished(level);
    }

    public void receiveUserMove(UserMove move) {
        logger.info("Received user move: " + move);
        try {
            sudoku.setValue(move.row(), move.col(), move.value());
            play(); // continue playing
        } catch (IllegalArgumentException e) {
            gameInterface.onInvalidMove(move.value(), move.row(), move.col());
        }
    }
}
