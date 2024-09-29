package org.sudokusolver.Strategy;

import org.sudokusolver.Gameplay.Solver.DifficultyLevel;
import org.sudokusolver.Strategy.Regions.RegionManager;

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
