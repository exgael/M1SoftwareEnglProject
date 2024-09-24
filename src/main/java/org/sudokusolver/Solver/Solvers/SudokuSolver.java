package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Regions.RegionManager;

public class SudokuSolver {

    private final Solver[] solvers;

    RegionManager regionManager;

    private SudokuSolver(Solver[] solvers) {
        this.solvers = solvers;

    }

    public static SudokuSolver build() {
        return new SudokuSolver(new Solver[]{
                SolverFactory.createEasySolver(),
                SolverFactory.createMediumSolver(),
                SolverFactory.createHardSolver(),
        });
    }

    public SudokuSolution findSudokuLevel(SudokuBoard sudoku) {
        int difficulty = 0;
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }
        for (var solver : solvers) {
            // Update sudoku level
            difficulty++;

            // Solve sudoku
            applySolver(solver);

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

    public void applySolver(@NotNull Solver solver) {
        boolean isSolving;
        do {
            isSolving = solver.solve(regionManager);
        }  while (isSolving);
    }
}
