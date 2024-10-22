package org.sudokusolver.Strategy.Solver.Rules;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Solver.DeductionRule;

import java.util.List;

public class CompositeRule implements DeductionRule {
    private final List<DeductionRule> rules;

    public CompositeRule(List<DeductionRule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean apply(Sudoku sudoku) {
        return rules.stream()
                .map(rule -> rule.apply(sudoku))
                .toList()
                .contains(true);
    }
}
