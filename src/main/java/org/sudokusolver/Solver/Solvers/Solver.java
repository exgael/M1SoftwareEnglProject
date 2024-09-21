package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;

import java.util.List;

public class Solver {

    private final List<DeductionRule> rules;

    public Solver(List<DeductionRule> rules) {
        this.rules = rules;
    }

    public void solve(SudokuBoard board) throws RuntimeException {
        for (int i = 0; i < board.getBoardSize(); i++) {
            applyRules(board);
        }
    }

    private void applyRules(SudokuBoard board) {
        rules.forEach((deductionRule -> deductionRule.apply(board)));
    }
}
