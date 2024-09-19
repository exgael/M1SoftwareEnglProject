package org.sudokusolver;

import org.sudokusolver.Solver.Solvers.SudokuSolution;
import org.sudokusolver.Solver.Solvers.SudokuSolver;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sudoku Solver");

        int[] easySudoku = new int[]{
                2, 9, 0, 0, 7, 1, 0, 0, 0,
                0, 8, 0, 3, 0, 9, 0, 0, 6,
                0, 4, 0, 0, 0, 0, 0, 0, 0,
                9, 0, 7, 0, 8, 0, 2, 0, 4,
                0, 0, 0, 9, 0, 0, 6, 0, 0,
                0, 0, 8, 0, 2, 0, 9, 1, 3,
                0, 2, 9, 7, 0, 4, 0, 3, 8,
                8, 0, 5, 1, 0, 0, 0, 7, 9,
                0, 7, 4, 0, 9, 0, 1, 6, 2
        };

        SudokuSolver sudokuSolver = SudokuSolver.build();
        SudokuSolution sol = sudokuSolver.findSudokuLevel(easySudoku);
        System.out.println(sol);
    }
}