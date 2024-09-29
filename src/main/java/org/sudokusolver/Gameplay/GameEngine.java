package org.sudokusolver.Gameplay;

import org.sudokusolver.File.SudokuFileParser;
import org.sudokusolver.Gameplay.Solver.SudokuSolution;
import org.sudokusolver.Gameplay.Solver.SudokuSolver;
import org.sudokusolver.Gameplay.Sudoku.Sudoku;
import org.sudokusolver.Gameplay.Sudoku.SudokuBoard;
import org.sudokusolver.Gameplay.Sudoku.SudokuCellUpdate;
import org.sudokusolver.Utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEngine {

    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());
    private final SudokuSolver solver;
    SudokuFileParser fileParser;
    private Sudoku sudoku;
    private final List<Observer<SudokuCellUpdate>> listeners = new ArrayList<>();

    public GameEngine(SudokuFileParser fileParser, SudokuSolver solver) {
        this.fileParser = fileParser;
        this.solver = solver;
    }

    public void addListener(Observer<SudokuCellUpdate> listener) {
        listeners.add(listener);
    }

    public void playNewSudoku(String filename) throws IOException {
        int[] board = fileParser.parseFileTo1DArray(filename);
        sudoku = new SudokuBoard(board);
        registerListenerToCurrentBoard();
        solve();
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

    public void solve() {
        var sol = solver.trySolveSudoku(sudoku);
        logResult(sol);
    }

    public void logResult(SudokuSolution testSol) {
        logger.log(Level.INFO, "Sudoku solved. Difficulty found: " + testSol.difficultyLevel());
        logger.log(Level.INFO, testSol.sudoku().debugDescription());
    }
}
