package org.sudokusolver;

import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Solvers.SudokuSolution;
import org.sudokusolver.Solver.Solvers.SudokuSolver;
import org.sudokusolver.Utils.SudokuGrids;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sudoku Solver");

        SudokuSolver sudokuSolver = SudokuSolver.build();

        // Easy
        SudokuBoard easyBoard = new SudokuBoard(SudokuGrids.easySudoku);
        SudokuSolution easySol = sudokuSolver.findSudokuLevel(easyBoard);
        System.out.println("Sudoku solved. Difficulty found: " + easySol.difficulty());
        System.out.println(easySol.sudokuBoard().debugDescription());

        // Medium
        SudokuBoard mediumBoard = new SudokuBoard(SudokuGrids.mediumSudoku);
        SudokuSolution mediumSol = sudokuSolver.findSudokuLevel(mediumBoard);
        System.out.println("Sudoku solved. Difficulty found: " + mediumSol.difficulty());
        System.out.println(mediumSol.sudokuBoard().debugDescription());

        // Hard
        SudokuBoard hardBoard = new SudokuBoard(SudokuGrids.hardSudoku);
        SudokuSolution hardSol = sudokuSolver.findSudokuLevel(hardBoard);
        System.out.println("Sudoku solved. Difficulty found: " + hardSol.difficulty());
        System.out.println(hardSol.sudokuBoard().debugDescription());

        // Master
        SudokuBoard masterBoard = new SudokuBoard(SudokuGrids.masterSudoku);
        SudokuSolution masterSol = sudokuSolver.findSudokuLevel(masterBoard);
        System.out.println("Sudoku solved. Difficulty found: " + masterSol.difficulty());
        System.out.println(masterSol.sudokuBoard().debugDescription());
    }
}