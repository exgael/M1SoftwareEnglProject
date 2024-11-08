package org.sudokusolver.Strategy.SolvingStrategies;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Strategy.Rules.DR1;
import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Strategy.SolvingStrategy;

public class EasySolvingStrategy implements SolvingStrategy {
    private final DeductionRule compositeRule;

    public EasySolvingStrategy() {
        this.compositeRule = new DR1();
    }

    @Override
    public boolean solve(Solvable sudoku) {
        return compositeRule.apply(sudoku);
    }
}
