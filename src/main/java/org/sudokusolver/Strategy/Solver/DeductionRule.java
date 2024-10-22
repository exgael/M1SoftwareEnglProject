package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(RegionManager regionManager, Sudoku sudoku);
}
