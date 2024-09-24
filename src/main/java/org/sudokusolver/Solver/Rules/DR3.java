package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Solver.Regions.Region;
import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Regions.RegionManager;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DR3 implements DeductionRule {

    // TODO : Use combinations to generalize for any naked count

    @Override
    public boolean apply(RegionManager regionManager) {
        // TODO: Determinate order of execution
       // board.forEachRegion(this::applyNakedPair);
       // board.forEachRegion(this::applyNakedTriple);
        return false;
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

    private void applyNakedTriple(Region region) {
        List<SudokuCell> candidateCells = region.findCellsWithCandidateCount(3);

        for (int i = 0; i < candidateCells.size(); i++) {
            var cell = candidateCells.get(i);
            // Find another two cells with same candidates
            for (int j = i + 1; j < candidateCells.size(); j++) {
                var otherCell = candidateCells.get(j);
                for (int k = j + 1; k < candidateCells.size(); k++) {
                    var anotherCell = candidateCells.get(k);
                    // Check for naked triple
                    if (isNakedTriple(cell, otherCell, anotherCell)) {
                        Set<Integer> candidates = cell.getCandidates();
                        Set<SudokuCell> triple = new HashSet<>(List.of(cell, otherCell, anotherCell));
                        region.removeCandidates(candidates, triple);
                        break; // Only one naked triple per region
                    }
                }
            }
        }
    }

    private boolean isNakedTriple(SudokuCell cell1, SudokuCell cell2, SudokuCell cell3) {
        return cell1.getCandidates().equals(cell2.getCandidates()) &&
               cell1.getCandidates().equals(cell3.getCandidates());
    }
}
