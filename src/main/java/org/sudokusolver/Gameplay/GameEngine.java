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
    private GameInterface gameInterface;
    private Sudoku sudoku;

    private final List<Observer<SudokuCellUpdate>> listeners = new ArrayList<>();

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

    public void playNewSudoku(String filename) throws IOException {
        logger.info("Starting new game with file: " + filename);
        initGame(filename);
        logger.info("Game initialized");
        playRound();
        logger.info("Game finished");
    }

    private void initGame(String filename) throws IOException {
        int[] board = fileParser.parseFileTo1DArray(filename);

        // Create a new sudoku board
        sudoku = new SudokuBoard();

        // Register listeners to the board
        registerListenerToCurrentBoard();

        // Now that listeners are registered, we can initialize the board
        sudoku.init(board);
    }

    private void playRound() {
        logger.info("Starting new round");
        while (!sudoku.isSolved()) {
            logger.info("Solver trying to solve sudoku");
            var solverResult = solver.trySolveSudoku(sudoku);
            if (sudoku.isSolved()) {
                logger.info("Sudoku solved");
                finishGame(solverResult);
                break;
            } else {
                logger.info("Sudoku not solved");
                getUserInputAndUpdateBoard();
            }
        }
    }

    private void finishGame(int level) {
        System.out.println(sudoku.debugDescription());
        gameInterface.onSudokuFinished(level);
    }

    private void getUserInputAndUpdateBoard() {
        logger.info("Requesting user input");
        UserMove userMove = gameInterface.onRequestUserInput();
        logger.info("User input received: " + userMove);

        try {
            sudoku.setValue(userMove.row(), userMove.col(), userMove.value());
        } catch (IllegalArgumentException e) {
            gameInterface.onInvalidMove(userMove.value(), userMove.row(), userMove.col());
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
