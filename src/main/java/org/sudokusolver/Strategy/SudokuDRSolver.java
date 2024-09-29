package org.sudokusolver.Strategy;

import org.jetbrains.annotations.Nullable;
import org.sudokusolver.Gameplay.Solver.DifficultyLevel;
import org.sudokusolver.Gameplay.Solver.SudokuSolution;
import org.sudokusolver.Gameplay.Solver.SudokuSolver;
import org.sudokusolver.Gameplay.Sudoku.Sudoku;
import org.sudokusolver.Strategy.Regions.RegionManager;

public class SudokuDRSolver implements SudokuSolver {
    RegionManager regionManager;

    /**
     * Try to solve the Sudoku puzzle and return the solution.
     *
     * @param sudoku the Sudoku puzzle
     * @return the Sudoku solution
     */
    @Override
    public SudokuSolution trySolveSudoku(Sudoku sudoku) {
        initializeRegionManager(sudoku);
        DifficultyLevel difficulty = solve(sudoku);
        cleanUpRegionManager();
        return new SudokuSolution(sudoku, difficulty);
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
            solver.applySolver(regionManager);

            if (sudoku.isSolved()) {
                isSolved = true;
            } else {
                difficulty = difficulty.getNext();
            }

        } while (difficulty != null && !isSolved);
        return difficulty;
    }


    /**
     * Initialize the region manager.
     * This is necessary if the SudokuSolver is used multiple times for different Sudoku puzzles.
     *
     * @param sudoku the Sudoku puzzle
     */
    private void initializeRegionManager(Sudoku sudoku) {
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }
    }

    /**
     * Clean up the region manager.
     * This is necessary if the SudokuSolver is used multiple times for different Sudoku puzzles.
     */
    private void cleanUpRegionManager() {
        regionManager = null;
    }
}
