package org.sudokusolver.Solver.Solvers;

import org.sudokusolver.Solver.Regions.RegionManager;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(RegionManager regionManager);
}
