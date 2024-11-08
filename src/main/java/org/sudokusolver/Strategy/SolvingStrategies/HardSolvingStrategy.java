package org.sudokusolver.Strategy.SolvingStrategies;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Strategy.Rules.CompositeRule;
import org.sudokusolver.Strategy.Rules.DR1;
import org.sudokusolver.Strategy.Rules.DR2;
import org.sudokusolver.Strategy.Rules.DR3;
import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Strategy.SolvingStrategy;

import java.util.List;

public class HardSolvingStrategy implements SolvingStrategy {
    private final DeductionRule compositeRule;

    public HardSolvingStrategy() {
        this.compositeRule = new CompositeRule(List.of(new DR1(), new DR2(), new DR3()));
    }

    @Override
    public boolean solve(Solvable sudoku) {
        return compositeRule.apply(sudoku);
    }
}