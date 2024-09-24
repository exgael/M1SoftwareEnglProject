package org.sudokusolver.Solver.Solvers;

import org.sudokusolver.Solver.Regions.RegionManager;

import java.util.List;

public class Solver {

    private final List<DeductionRule> rules;

    public Solver(List<DeductionRule> rules) {
        this.rules = rules;
    }

    public boolean solve(RegionManager regionManager) throws RuntimeException {
        return rules.stream()
                .map(rule -> rule.apply(regionManager))
                .toList()
                .contains(true);
    }
}
