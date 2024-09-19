package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Solvers.DeductionRule;

public class DR1 implements DeductionRule {
    @Override
    public boolean apply(SudokuBoard board) {
        return false;
    }
}