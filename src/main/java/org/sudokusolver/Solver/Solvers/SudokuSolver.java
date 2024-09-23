package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Solver.Regions.RegionManager;

import java.util.List;

public class SudokuSolver {

    private final Solver[] solvers;

    RegionManager regionManager;

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

    public SudokuSolution findSudokuLevel(SudokuBoard sudoku) {
        int difficulty = 0;
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }
        for (var solver : solvers) {
            difficulty++;
            solver.solve(regionManager);
            if (sudoku.isSolved()) {
                int[] linearizedBoard = finalizeSolution(sudoku);
                return new SudokuSolution(linearizedBoard, difficulty);
            }
        }
        return new SudokuSolution(null, -1);
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
