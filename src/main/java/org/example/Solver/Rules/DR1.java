package org.example.Solver.Rules;

import org.example.Core.SudokuBoard;
import org.example.Solver.Solvers.DeductionRule;

public class DR1 implements DeductionRule {
    @Override
    public boolean apply(SudokuBoard board) {
        return false;
    }
}