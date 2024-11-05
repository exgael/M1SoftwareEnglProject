package org.sudokusolver;

import org.sudokusolver.GameInterface.SudokuView;
import org.sudokusolver.Utils.Reader.GridLoader;
import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuBoard;
import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.SudokuSolver;
import org.sudokusolver.GameInterface.SudokuController;
import org.sudokusolver.Strategy.Solver.SudokuDRSolver;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        logger.info("Starting Sudoku Solver");

        // Initialize Strategy Module
        SudokuSolver solver = new SudokuDRSolver();
        Sudoku sudoku = new SudokuBoard();

        // Initialize Gameplay Module
        GridLoader gridLoader = new GridLoader();
        GameEngine gameEngine = new GameEngine(gridLoader, solver, sudoku);

        // Initialize GUI
        SudokuController controller = new SudokuController(gameEngine, null);
        new SudokuView(controller);
    }
}