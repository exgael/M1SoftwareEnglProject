package org.sudokusolver.Strategy.SolvingStrategies;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.Rules.DR1;
import org.sudokusolver.Strategy.SolvingStrategy;

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
