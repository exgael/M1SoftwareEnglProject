package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Strategy.Solver.SolvingStrategies.EasySolvingStrategy;
import org.sudokusolver.Strategy.Solver.SolvingStrategies.HardSolvingStrategy;
import org.sudokusolver.Strategy.Solver.SolvingStrategies.MediumSolvingStrategy;

public class StrategyFactory {
    public static SolvingStrategy createStrategy(DifficultyLevel difficultyLevel) {
        return switch (difficultyLevel) {
            case EASY -> new EasySolvingStrategy();
            case MEDIUM -> new MediumSolvingStrategy();
            case HARD -> new HardSolvingStrategy();
            default -> throw new RuntimeException("Expert level not implemented yet");
        };
    }
}

