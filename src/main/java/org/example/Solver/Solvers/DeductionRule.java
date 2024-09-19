package org.example.Solver.Solvers;

import org.example.Core.SudokuBoard;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(SudokuBoard board);
}
