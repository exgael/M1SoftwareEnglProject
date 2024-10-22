package org.sudokusolver.Strategy.Solver.SolvingStrategies;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Rules.CompositeRule;
import org.sudokusolver.Strategy.Solver.Rules.DR1;
import org.sudokusolver.Strategy.Solver.Rules.DR2;
import org.sudokusolver.Strategy.Solver.Rules.DR3;
import org.sudokusolver.Strategy.Solver.SolvingStrategy;

import java.util.List;

public class HardSolvingStrategy implements SolvingStrategy {
    private final DeductionRule compositeRule;

    public HardSolvingStrategy() {
        this.compositeRule = new CompositeRule(List.of(new DR1(), new DR2(), new DR3()));
    }

    @Override
    public boolean solve(Sudoku sudoku) {
        return compositeRule.apply(sudoku);
    }
}