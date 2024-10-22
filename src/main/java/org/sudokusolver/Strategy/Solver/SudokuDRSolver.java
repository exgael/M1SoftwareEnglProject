package org.sudokusolver.Strategy.Solver;

import org.jetbrains.annotations.Nullable;
import org.sudokusolver.Gameplay.SudokuSolver;
import org.sudokusolver.Gameplay.Sudoku;

public class SudokuDRSolver implements SudokuSolver {

    /**
     * Try to solve the Sudoku puzzle and return the solution.
     *
     * @param sudoku the Sudoku puzzle
     * @return the Sudoku level
     */
    @Override
    public int trySolveSudoku(Sudoku sudoku) {
        DifficultyLevel difficulty = solve(sudoku);
        return difficulty == null ? -1 : difficulty.ordinal();
    }

    /**
     * Solve the Sudoku puzzle.
     *
     * @param sudoku the Sudoku puzzle
     * @return the difficulty level of the Sudoku puzzle or null if the puzzle is not solvable
     */
    @Nullable
    private DifficultyLevel solve(Sudoku sudoku) {
        DifficultyLevel difficulty = DifficultyLevel.EASY;
        boolean isSolved = false;
        Solver solver;

        do {
            solver = new Solver(difficulty);
            solver.applySolver(sudoku);

            if (sudoku.isSolved()) {
                isSolved = true;
            } else {
                difficulty = difficulty.getNext();
            }

        } while (difficulty != null && !isSolved);
        return difficulty;
    }
}
