package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuComponents.Regions.Region;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuComponents.SudokuCell;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DR3 implements DeductionRule {

    @Override
    public void apply(SudokuBoard board) {
        board.forEachRegion(this::applyNakedPair);
    }

    private void applyNakedPair(Region region) {
        List<SudokuCell> candidateCells = region.findCellsWithCandidateCount(2);

        for (int i = 0; i < candidateCells.size(); i++) {
            var cell = candidateCells.get(i);
            // Find another cell with same candidates
            for (int j = i + 1; j < candidateCells.size(); j++) {
                var otherCell = candidateCells.get(j);
                // Check for naked pair
                if (isNakedPair(cell, otherCell)) {
                    Set<Integer> candidates = cell.getCandidates();
                    Set<SudokuCell> pair = new HashSet<>(List.of(cell, otherCell));
                    region.removeCandidates(candidates, pair);
                    break; // Only one naked pair per region
                }
            }
        }
    }

    private boolean isNakedPair(SudokuCell cell1, SudokuCell cell2) {
        return cell1.getCandidates().equals(cell2.getCandidates());
    }

    private void applyNakedTriple(List<SudokuCell> threeCandidateCells, List<SudokuCell> unsolvedCells) {
        // TODO
    }
}
