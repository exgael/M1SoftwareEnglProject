package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(RegionManager regionManager);
}
