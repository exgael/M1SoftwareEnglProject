package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuComponents.Regions.Region;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuComponents.SudokuCell;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.List;
import java.util.Set;

public class DR3 implements DeductionRule {

    @Override
    public void apply(SudokuBoard board) {
        board.forEachRegion(this::applyNakedPair);
    }

    private void applyNakedPair(Region region) {
        List<SudokuCell> unsolvedCells = region.findUnsolvedCells();
        List<SudokuCell> twoCandidatesCells = region.findCellsWithCandidateCount(2);

        for (int i = 0; i < twoCandidatesCells.size(); i++) {
            var cell = twoCandidatesCells.get(i);
            // Find another cell with same candidates
            for (int j = i + 1; j < twoCandidatesCells.size(); j++) {
                var otherCell = twoCandidatesCells.get(j);
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
