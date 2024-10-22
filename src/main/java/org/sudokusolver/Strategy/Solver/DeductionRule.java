package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Gameplay.Solvable;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(Solvable sudoku);
}
