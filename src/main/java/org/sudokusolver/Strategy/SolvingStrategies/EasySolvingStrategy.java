package org.sudokusolver.Strategy.SolvingStrategies;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.Rules.DR1;
import org.sudokusolver.Strategy.SolvingStrategy;

import java.util.List;

public class EasySolvingStrategy implements SolvingStrategy {
    private final List<DeductionRule> rules;

    public EasySolvingStrategy() {
        this.rules = List.of(new DR1());
    }

    @Override
    public boolean solve(RegionManager regionManager) {
        return rules.stream()
                .map(rule -> rule.apply(regionManager))
                .toList()
                .contains(true);
    }
}
