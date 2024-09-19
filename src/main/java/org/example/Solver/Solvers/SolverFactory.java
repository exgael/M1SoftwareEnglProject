package org.example.Solver.Solvers;

import org.example.Solver.Rules.DR1;
import org.example.Solver.Rules.DR2;
import org.example.Solver.Rules.DR3;

import java.util.ArrayList;
import java.util.List;

public class SolverFactory {
    public static Solver createEasySolver() {
        List<DeductionRule> rules = new ArrayList<>();
        rules.add(new DR1());

        return new Solver(rules);
    }

    public static Solver createMediumSolver() {
        List<DeductionRule> rules = new ArrayList<>();
        rules.add(new DR1());
        rules.add(new DR2());
        return new Solver(rules);
    }

    public static Solver createHardSolver() {
        List<DeductionRule> rules = new ArrayList<>();
        rules.add(new DR1());
        rules.add(new DR2());
        rules.add(new DR3());
        return new Solver(rules);
    }
}

