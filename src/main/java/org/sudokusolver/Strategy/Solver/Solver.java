package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

public class Solver {

    private final SolvingStrategy strategy;

    public Solver(DifficultyLevel level) {
        this.strategy = StrategyFactory.createStrategy(level);
    }

    public void applySolver(RegionManager regionManager) {
        boolean isSolving;
        do {
            isSolving = strategy.solve(regionManager);
        } while (isSolving);
    }
}
