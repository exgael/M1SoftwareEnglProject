package org.sudokusolver.Strategy.Solver.SolvingStrategies;

import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Rules.DR1;
import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Strategy.Solver.SolvingStrategy;

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
