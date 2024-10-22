package org.sudokusolver.Strategy.Solver.SolvingStrategies;

import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Rules.CompositeRule;
import org.sudokusolver.Strategy.Solver.Rules.DR1;
import org.sudokusolver.Strategy.Solver.Rules.DR2;
import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Strategy.Solver.SolvingStrategy;

import java.util.List;

public class MediumSolvingStrategy implements SolvingStrategy {
    private final DeductionRule compositeRule;

    public MediumSolvingStrategy() {
        this.compositeRule = new CompositeRule(List.of(new DR1(), new DR2()));
    }

    @Override
    public boolean solve(Solvable sudoku) {
        return compositeRule.apply(sudoku);
    }
}
