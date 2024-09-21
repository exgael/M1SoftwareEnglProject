package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;

import java.util.List;

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

        SudokuBoard board = initialize(linearBoard);

        for (Solver solver : solvers) {
            solver.solve(board); // Reuse grid from previous solving attempt.
            difficulty++;
            if (board.isSolved()) {
                int[] linearizedBoard = finalizeSolution(board);
                return new SudokuSolution(linearizedBoard, difficulty);
            }
        }

        return new SudokuSolution(null, -1);
    }

    private @NotNull SudokuBoard initialize(int @NotNull [] linearBoard) {
        return new SudokuBoard(linearBoard);
    }

    private int @NotNull [] finalizeSolution(@NotNull SudokuBoard board) {
        int[] linearizedGrid = new int[board.getRows() * board.getCols()];

        for (int i = 0; i < linearizedGrid.length; i++) {
            // switch row each 9th increment
            int row = i / board.getRows();

            // Switch col every increment up until 8 and restart
            int col = i % board.getCols();
            linearizedGrid[i] = board.getValue(row, col);
        }

        return linearizedGrid;
    }
}
