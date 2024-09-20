package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.List;

public class DR3 implements DeductionRule {

    private boolean applyNakedPair(List<SudokuCell> unresolved) {
        for (int i = 0; i < unresolved.size(); i++) {
            var cell = unresolved.get(i);
            if (cell.candidateCount() == 2) {
                // Find another cell with same candidates
                for (var other : unresolved) {
                    if (cell.equals(other)) continue;
                    // Check for naked pair
                    if (cell.getCandidates().equals(other.getCandidates())) {
                        // Remove those candidates from all the other unresolved cells
                        unresolved.forEach(sudokuCell -> {
                            if (!sudokuCell.equals(cell) && !sudokuCell.equals(other)) {
                                cell.getCandidates().forEach(sudokuCell::removeCandidate);
                            }
                        });
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean apply(SudokuBoard board) {
        boolean succeeded = false;
        for (int i = 0; i < board.getBoardSize(); i++) {
            if (applyNakedPairToRow(board, i)) {
                succeeded = true;
            }

            if (applyNakedPairToColumn(board, i)) {
                succeeded = true;
            }
        }

        for (int i = 0; i < board.getBoardSize(); i += board.getSubgridSize()) {
            for (int j = 0; j < board.getBoardSize(); j += board.getSubgridSize()) {
                if (applyNakedPairToSubgrid(board, i, j)) {
                    succeeded = true;
                }
            }
        }

        return succeeded;
    }

    private boolean applyNakedPairToRow(SudokuBoard board, int row) {
        List<SudokuCell> unsolvedCell = board.findUnsolvedCellsInRow(row);
        return applyNakedPair(unsolvedCell);
    }

    private boolean applyNakedPairToColumn(SudokuBoard board, int col) {
        List<SudokuCell> unsolvedCell = board.findUnsolvedCellsInColumns(col);
        return applyNakedPair(unsolvedCell);
    }

    private boolean applyNakedPairToSubgrid(SudokuBoard board, int row, int col) {
        List<SudokuCell> unsolvedCell = board.findUnsolvedCellsInSubgrid(row, col);
        return applyNakedPair(unsolvedCell);
    }
}
