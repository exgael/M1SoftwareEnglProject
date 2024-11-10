package org.sudokusolver.Strategy;

import org.sudokusolver.Strategy.SolvingStrategies.EasySolvingStrategy;
import org.sudokusolver.Strategy.SolvingStrategies.HardSolvingStrategy;
import org.sudokusolver.Strategy.SolvingStrategies.MediumSolvingStrategy;

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

