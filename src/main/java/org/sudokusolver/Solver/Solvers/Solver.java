package org.sudokusolver.Solver.Solvers;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Core.SudokuBoard;

import java.util.List;

public class Solver {

    private final List<DeductionRule> rules;
    private boolean isSolved = false;

    public Solver(List<DeductionRule> rules) {
        this.rules = rules;
    }

    public int[] solve(int[] linearBoard) throws RuntimeException {
        SudokuBoard board = initialize(linearBoard);

        for (int i = 0; i < linearBoard.length; i++) {
            applyRules(board);
        }

        return finalizeSolution(board);
    }

    public boolean succeeded() {
        return isSolved;
    }

    private void applyRules(SudokuBoard board) {
        rules.forEach((deductionRule -> deductionRule.apply(board)));
    }

    private @NotNull SudokuBoard initialize(int @NotNull [] linearBoard) {
        SudokuBoard board = new SudokuBoard();

        for (int i = 0; i < linearBoard.length; i++) {
            // switch row each 9th increment
            int row = i / board.getRows();

            // Switch col every increment up until 8 and restart
            int col = i % board.getCols();
            board.setValue(row, col, linearBoard[i]);
        }
        return board;
    }

    private int @NotNull [] finalizeSolution(@NotNull SudokuBoard board) {
        this.isSolved = true;

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
