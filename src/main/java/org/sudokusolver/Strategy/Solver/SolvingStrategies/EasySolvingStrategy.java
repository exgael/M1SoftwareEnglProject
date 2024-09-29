package org.sudokusolver.Strategy.Solver.SolvingStrategies;

import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;
import org.sudokusolver.Strategy.Solver.Rules.DR1;
import org.sudokusolver.Strategy.Solver.SolvingStrategy;

public class EasySolvingStrategy implements SolvingStrategy {
    private final DeductionRule compositeRule;

    public EasySolvingStrategy() {
        this.compositeRule = new DR1();
    }

    @Override
    public boolean solve(RegionManager regionManager) {
        return compositeRule.apply(regionManager);
    }
}
