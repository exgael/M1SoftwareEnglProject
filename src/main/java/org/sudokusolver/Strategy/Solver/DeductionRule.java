package org.sudokusolver.Strategy.Solver;

import org.sudokusolver.Gameplay.Sudoku;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(Sudoku sudoku);
}
