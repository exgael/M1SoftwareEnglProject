package org.sudokusolver.Strategy.Solver;

import org.jetbrains.annotations.Nullable;
import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Gameplay.SudokuSolver;

public class SudokuDRSolver implements SudokuSolver {

    private DifficultyLevel highestDifficulty = DifficultyLevel.EASY;
    /**
     * Try to solve the Sudoku puzzle and return the solution.
     *
     * @param sudoku the Sudoku puzzle
     * @return the Sudoku level
     */

    @Override
    public int trySolveSudoku(Solvable sudoku) {
        DifficultyLevel difficulty = solve(sudoku);
        return difficulty == null ? -1 : highestDifficulty.ordinal();
    }

    /**
     * Solve the Sudoku puzzle.
     *
     * @param sudoku the Sudoku puzzle
     * @return the difficulty level of the Sudoku puzzle or null if the puzzle is not solvable
     */
    @Nullable
    private DifficultyLevel solve(Solvable sudoku) {
        DifficultyLevel difficulty = DifficultyLevel.EASY;
        boolean isSolved = false;
        Solver solver;

        do {
            solver = new Solver(difficulty);
            solver.applySolver(sudoku);

            if (sudoku.isSolved()) {
                isSolved = true;
            } else {
                if (difficulty.ordinal() > highestDifficulty.ordinal()) {
                    highestDifficulty = difficulty;
                }
                difficulty = difficulty.getNext();
            }

        } while (difficulty != null && !isSolved);
        return isSolved ? highestDifficulty : null;
    }
}
