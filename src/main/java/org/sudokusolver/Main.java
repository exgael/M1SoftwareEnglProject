package org.sudokusolver;

import org.sudokusolver.File.SudokuFileParser;
import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Strategy.SudokuSolution;
import org.sudokusolver.Strategy.SudokuSolver;
import org.sudokusolver.Utils.SudokuGrids;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sudoku Solver");

        SudokuSolver sudokuSolver = new SudokuSolver();

        // Test Read File
        String fileName = "/org/sudokusolver/example1_easy.txt";
        SudokuFileParser fileParser = new SudokuFileParser();
        int[] board = fileParser.parseFileTo1DArray(fileName);
        SudokuBoard someBoard = new SudokuBoard(board);
        SudokuSolution testSol = sudokuSolver.findSudokuLevel(someBoard);
        System.out.println("Sudoku solved. Difficulty found: " + testSol.difficultyLevel());
        System.out.println(testSol.sudokuBoard().debugDescription());

        // Easy
        SudokuBoard easyBoard = new SudokuBoard(SudokuGrids.easySudoku);
        SudokuSolution easySol = sudokuSolver.findSudokuLevel(easyBoard);
        System.out.println("Sudoku solved. Difficulty found: " + easySol.difficultyLevel());
        System.out.println(easySol.sudokuBoard().debugDescription());

        // Medium
        SudokuBoard mediumBoard = new SudokuBoard(SudokuGrids.mediumSudoku);
        SudokuSolution mediumSol = sudokuSolver.findSudokuLevel(mediumBoard);
        System.out.println("Sudoku solved. Difficulty found: " + mediumSol.difficultyLevel());
        System.out.println(mediumSol.sudokuBoard().debugDescription());

        // Hard
        SudokuBoard hardBoard = new SudokuBoard(SudokuGrids.hardSudoku);
        SudokuSolution hardSol = sudokuSolver.findSudokuLevel(hardBoard);
        System.out.println("Sudoku solved. Difficulty found: " + hardSol.difficultyLevel());
        System.out.println(hardSol.sudokuBoard().debugDescription());

        // Master
        SudokuBoard masterBoard = new SudokuBoard(SudokuGrids.masterSudoku);
        SudokuSolution masterSol = sudokuSolver.findSudokuLevel(masterBoard);
        System.out.println("Sudoku solved. Difficulty found: " + masterSol.difficultyLevel());
        System.out.println(masterSol.sudokuBoard().debugDescription());
    }
}