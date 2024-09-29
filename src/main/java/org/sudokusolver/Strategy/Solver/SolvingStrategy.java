package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

@FunctionalInterface
public interface SolvingStrategy {

    /**
     * Solves the Sudoku puzzle using the strategy.
     *
     * @param regionManager the region manager
     * @return true if the strategy was able to make new deduction (1 or more)
     * @throws RuntimeException if the strategy encounters an error
     */
    boolean solve(RegionManager regionManager) throws RuntimeException;
}
