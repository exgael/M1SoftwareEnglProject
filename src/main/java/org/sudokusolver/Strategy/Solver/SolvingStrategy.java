package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Gameplay.Sudoku;

@FunctionalInterface
public interface SolvingStrategy {

    /**
     * Solves the Sudoku puzzle using the strategy.
     *
     * @param sudoku the sudoku game
     * @return true if the strategy was able to make new deduction (1 or more)
     * @throws RuntimeException if the strategy encounters an error
     */
    boolean solve(Sudoku sudoku) throws RuntimeException;
}
