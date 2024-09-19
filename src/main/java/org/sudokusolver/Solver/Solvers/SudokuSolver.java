package org.sudokusolver.Solver.Solvers;

public class SudokuSolver {

    private final Solver[] solvers;

    private SudokuSolver(Solver[] solvers) {
        this.solvers = solvers;
    }

    public static SudokuSolver build() {
        return new SudokuSolver(new Solver[]{
                SolverFactory.createEasySolver(),
                SolverFactory.createMediumSolver(),
                SolverFactory.createHardSolver(),
        });
    }

    public SudokuSolution findSudokuLevel(int[] linearBoard) {
        int difficulty = 0; // The level of the grid is intrinsic to the number of solver used.

        int[] gridState = linearBoard.clone();

        for (Solver solver : solvers) {
            gridState = solver.solve(gridState); // Reuse grid from previous solving attempt.
            difficulty++;

            if (solver.succeeded()) {
                new SudokuSolution(gridState, difficulty);
            }
        }

        return new SudokuSolution(null, -1);
    }
}
