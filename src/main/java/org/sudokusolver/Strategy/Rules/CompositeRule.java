package org.sudokusolver.Strategy.Rules;

import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Gameplay.Solvable;

import java.util.List;

public class CompositeRule implements DeductionRule {
    private final List<DeductionRule> rules;

    public CompositeRule(List<DeductionRule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean apply(Solvable sudoku) {
        return rules.stream()
                .map(rule -> rule.apply(sudoku))
                .toList()
                .contains(true);
    }
}
