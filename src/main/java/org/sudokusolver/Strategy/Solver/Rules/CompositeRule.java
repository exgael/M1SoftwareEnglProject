package org.sudokusolver.Strategy.Solver.Rules;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

import java.util.List;

public class CompositeRule implements DeductionRule {
    private final List<DeductionRule> rules;

    public CompositeRule(List<DeductionRule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean apply(RegionManager regionManager, Sudoku sudoku) {
        return rules.stream()
                .map(rule -> rule.apply(regionManager, sudoku))
                .toList()
                .contains(true);
    }
}
