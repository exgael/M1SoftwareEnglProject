package org.sudokusolver.Strategy.SolvingStrategies;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.Rules.DR1;
import org.sudokusolver.Strategy.Rules.DR2;
import org.sudokusolver.Strategy.SolvingStrategy;

import java.util.List;

public class MediumSolvingStrategy implements SolvingStrategy {
    private final List<DeductionRule> rules;

    public MediumSolvingStrategy() {
        this.rules = List.of(new DR1(), new DR2());
    }

    @Override
    public boolean solve(RegionManager regionManager) {
        return rules.stream()
                .map(rule -> rule.apply(regionManager))
                .toList()
                .contains(true);
    }
}
