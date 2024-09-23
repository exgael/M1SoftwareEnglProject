package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Regions.RegionManager;

import java.util.List;

public class Solver {

    private final List<DeductionRule> rules;

    public Solver(List<DeductionRule> rules) {
        this.rules = rules;
    }

    public void solve(RegionManager regionManager) throws RuntimeException {
        rules.forEach((deductionRule -> deductionRule.apply(regionManager)));
    }
}
