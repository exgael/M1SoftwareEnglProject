package org.sudokusolver.Gameplay;

import org.sudokusolver.File.SudokuFileParser;
import org.sudokusolver.Gameplay.Solver.SudokuSolution;
import org.sudokusolver.Gameplay.Solver.SudokuSolver;
import org.sudokusolver.Gameplay.Sudoku.Sudoku;
import org.sudokusolver.Gameplay.Sudoku.SudokuBoard;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEngine {

    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());
    private final SudokuSolver solver;
    SudokuFileParser fileParser;
    private Sudoku sudoku;

    public GameEngine(SudokuFileParser fileParser, SudokuSolver solver) {
        this.fileParser = fileParser;
        this.solver = solver;
    }

    public void playNewSudoku(String filename) throws IOException {
        int[] board = fileParser.parseFileTo1DArray(filename);
        sudoku = new SudokuBoard(board);
        solve();
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
