package org.sudokusolver.Solver.Solvers;

import org.sudokusolver.Core.SudokuBoard;

@FunctionalInterface
public interface DeductionRule {
    void apply(SudokuBoard board);
}
