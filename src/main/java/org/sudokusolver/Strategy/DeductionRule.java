package org.sudokusolver.Strategy;

import org.sudokusolver.Gameplay.Solvable;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(Solvable sudoku);
}
