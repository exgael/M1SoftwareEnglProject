package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Gameplay.Solvable;

public class Solver {

    private final SolvingStrategy strategy;

    public Solver(DifficultyLevel level) {
        this.strategy = StrategyFactory.createStrategy(level);
    }

    public void applySolver(Solvable sudoku) {
        boolean isSolving;
        do {
            isSolving = strategy.solve(sudoku);
        } while (isSolving);
    }
}
