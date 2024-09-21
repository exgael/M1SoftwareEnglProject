package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.RegionType;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.List;
import java.util.Set;

import static org.sudokusolver.Core.RegionType.*;

public class DR3 implements DeductionRule {

    @Override
    public void apply(SudokuBoard board) {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                applyNakedPairsAcrossRegions(board, i, j);
            }
        }
    }

    private void applyNakedPairsAcrossRegions(SudokuBoard board, int row, int col) {
        this.applyNakedPairToRegion(board, row, col, ROW);
        this.applyNakedPairToRegion(board, row, col, COLUMN);
        this.applyNakedPairToRegion(board, row, col, SUBGRID);
    }

    private void applyNakedPairToRegion(SudokuBoard board, int row, int col, RegionType regionType) {
        List<SudokuCell> unsolvedCells = board.findUnsolvedCellsInRegion(row, col, regionType);
        List<SudokuCell> twoCandidatesCells = board.findCellsWithCandidateCountInRegion(row, col, 2, regionType);
        this.applyNakedPair(twoCandidatesCells, unsolvedCells);
    }

    private void applyNakedPair(List<SudokuCell> twoCandidateCells, List<SudokuCell> unsolvedCells) {
        for (int i = 0; i < twoCandidateCells.size(); i++) {
            var cell = twoCandidateCells.get(i);
            // Find another cell with same candidates
            for (int j = i + 1; j < twoCandidateCells.size(); j++) {
                var otherCell = twoCandidateCells.get(j);
                // Check for naked pair
                if (cell.getCandidates().equals(otherCell.getCandidates())) {
                    Set<Integer> candidates = cell.getCandidates();
                    // Remove those candidates from all the other unsolved cells
                    unsolvedCells.forEach(unsolvedCell -> {
                        if (!unsolvedCell.equals(cell) && !unsolvedCell.equals(otherCell)) {
                            cell.getCandidates().forEach(unsolvedCell::removeCandidate);
                        }
                    });
                    break; // This cell has already found its pair.
                }
            }
        }
    }


    private void applyNakedTriple(List<SudokuCell> threeCandidateCells, List<SudokuCell> unsolvedCells) {
        // TODO
    }
}
