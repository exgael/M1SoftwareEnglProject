package org.sudokusolver.Strategy;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.SolvingStrategies.EasySolvingStrategy;
import org.sudokusolver.Strategy.SolvingStrategies.HardSolvingStrategy;
import org.sudokusolver.Strategy.SolvingStrategies.MediumSolvingStrategy;

import java.util.Set;

public class SudokuSolver {

    private final Set<SolvingStrategy> strategies;

    RegionManager regionManager;

    public SudokuSolver() {
        this.strategies = Set.of(new EasySolvingStrategy(), new MediumSolvingStrategy(), new HardSolvingStrategy());
    }

    public SudokuSolution findSudokuLevel(SudokuBoard sudoku) {
        int difficulty = 0;
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }
        for (var strategy : strategies) {
            // Update sudoku level
            difficulty++;

            // Solve sudoku
            applySolver(strategy);

            // Check if sudoku is solved
            if (sudoku.isSolved()) {
                // Reset region manager
                regionManager = null;
                // if sudoku is solved, return the solution
                return new SudokuSolution(sudoku, difficulty);
            }
        }

        regionManager = null;
        // if sudoku is not solved, return the unsolved sudoku
        return new SudokuSolution(sudoku, -1);
    }

    public void applySolver(@NotNull SolvingStrategy strategy) {
        boolean isSolving;
        do {
            isSolving = strategy.solve(regionManager);
        } while (isSolving);
    }
}
