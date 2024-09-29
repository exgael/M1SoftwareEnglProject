package org.sudokusolver;

import org.sudokusolver.File.SudokuFileParser;
import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.Solver.SudokuSolver;
import org.sudokusolver.Gameplay.Solver.SudokuSolution;
import org.sudokusolver.Strategy.SudokuDRSolver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.out.println("Sudoku Solver");

        SudokuSolver solver = new SudokuDRSolver();
        SudokuFileParser fileParser = new SudokuFileParser();
        GameEngine gameEngine = new GameEngine(fileParser, solver);
        String[] filenames = getFileNames();

        for (String filename : filenames) {
            try {
                gameEngine.playNewSudoku(filename);
            } catch (IOException e) {
                handleFileError(filename, e);
            } catch (Exception e) {
                handleGeneralError(filename, e);
            }
        }
    }

    private static void handleFileError(String filename, IOException e) {
        logger.log(Level.SEVERE, "Erreur lors de la lecture du fichier : " + filename, e);
    }

    private static void handleGeneralError(String filename, Exception e) {
        logger.log(Level.SEVERE, "Une erreur inattendue s'est produite lors du traitement du fichier : " + filename, e);
    }

    private static String[] getFileNames() {
        return new String[]{
                "/sudoku_data/easy1.txt",
                "/sudoku_data/easy2.txt",
                "/sudoku_data/easy3.txt",
                "/sudoku_data/easy4.txt",
                "/sudoku_data/easy5.txt",
                "/sudoku_data/easy6.txt",
                "/sudoku_data/easy7.txt",
                "/sudoku_data/easy8.txt",
                "/sudoku_data/easy9.txt",
                "/sudoku_data/easy10.txt",
                "/sudoku_data/medium1.txt",
                "/sudoku_data/medium2.txt",
                "/sudoku_data/medium3.txt",
                "/sudoku_data/medium4.txt",
                "/sudoku_data/medium5.txt",
                "/sudoku_data/medium6.txt",
                "/sudoku_data/medium7.txt",
                "/sudoku_data/medium8.txt",
                "/sudoku_data/medium9.txt",
                "/sudoku_data/medium10.txt",
                "/sudoku_data/hard1.txt",
                "/sudoku_data/hard2.txt",
                "/sudoku_data/hard3.txt",
                "/sudoku_data/hard4.txt",
                "/sudoku_data/hard5.txt",
                "/sudoku_data/hard6.txt",
                "/sudoku_data/hard7.txt",
                "/sudoku_data/hard8.txt",
                "/sudoku_data/hard9.txt",
                "/sudoku_data/hard10.txt",
                "/sudoku_data/master1.txt",
                "/sudoku_data/master2.txt",
                "/sudoku_data/master3.txt",
                "/sudoku_data/master4.txt",
                "/sudoku_data/master5.txt",
                "/sudoku_data/master6.txt",
                "/sudoku_data/master7.txt",
                "/sudoku_data/master8.txt",
                "/sudoku_data/master9.txt",
                "/sudoku_data/master10.txt"
        };
    }

}