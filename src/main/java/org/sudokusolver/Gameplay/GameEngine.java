package org.sudokusolver.Gameplay;

import org.sudokusolver.Utils.File.SudokuFileParser;
import org.sudokusolver.Strategy.Sudoku.SudokuBoard;
import org.sudokusolver.Strategy.Sudoku.SudokuCellUpdate;
import org.sudokusolver.Utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameEngine {

    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());
    private final SudokuSolver solver;
    private final SudokuFileParser fileParser;
    private final List<Observer<SudokuCellUpdate>> listeners = new ArrayList<>();
    private GameInterface gameInterface;
    private Sudoku sudoku;
    int[] initialBoard;


    public GameEngine(SudokuFileParser fileParser, SudokuSolver solver) {
        this.fileParser = fileParser;
        this.solver = solver;
    }

    public void setGameInterface(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }

    public void addListener(Observer<SudokuCellUpdate> listener) {
        listeners.add(listener);
    }

    public void loadNewSudoku(String filename) throws IOException {
        logger.info("Starting new game with file: " + filename);

        this.initialBoard = fileParser.parseFileTo1DArray(filename);
    }

    public void prepareBoard() {

        // Create a new sudoku board
        sudoku = new SudokuBoard();

        // Register listeners to the board
        registerListenerToCurrentBoard();

        // Now that listeners are registered, we can initialize the board
        sudoku.init(initialBoard);
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

    private void registerListenerToCurrentBoard() {
        listeners.forEach(this::registerListenerToCellUpdate);
    }

    private void registerListenerToCellUpdate(Observer<SudokuCellUpdate> listener) {
        for (int row = 0; row < sudoku.getBoardSize(); row++) {
            for (int col = 0; col < sudoku.getBoardSize(); col++) {
                sudoku.getElement(row, col).addObserver(listener);
            }
        }
    }
}
