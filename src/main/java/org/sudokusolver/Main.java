package org.sudokusolver;

import org.sudokusolver.Utils.File.GridLoader;
import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuBoard;
import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.SudokuSolver;
import org.sudokusolver.GameInterface.SudokuGUI;
import org.sudokusolver.Strategy.Solver.SudokuDRSolver;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.out.println("Sudoku Solver");

        SudokuSolver solver = new SudokuDRSolver();
        GridLoader gridLoader = new GridLoader();
        Sudoku sudoku = new SudokuBoard();
        GameEngine gameEngine = new GameEngine(gridLoader, solver, sudoku);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // No need to handle this
        }

        SwingUtilities.invokeLater(() -> {
            new SudokuGUI(gameEngine);
        });
    }
}