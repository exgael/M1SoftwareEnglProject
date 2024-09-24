package org.sudokusolver;

import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Solvers.SudokuSolution;
import org.sudokusolver.Solver.Solvers.SudokuSolver;
import org.sudokusolver.Utils.SudokuGrids;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sudoku Solver");
        SudokuBoard board = new SudokuBoard(SudokuGrids.hardSudoku);
        SudokuSolver sudokuSolver = SudokuSolver.build();
        SudokuSolution sol = sudokuSolver.findSudokuLevel(board);
        var solvedGrid = sol.sudokuBoard();

        System.out.println("Level: " + sol.difficulty());
        System.out.println(solvedGrid.debugDescription());
    }
}