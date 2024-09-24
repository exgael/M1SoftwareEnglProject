package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Regions.Region;
import org.sudokusolver.Solver.Regions.RegionManager;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DR2 implements DeductionRule {

    @Override
    public boolean apply(RegionManager regionManager) {
        return regionManager.stream()
                .map(this::applyNakedPair)
                .toList()
                .contains(true);
    }

    private boolean applyNakedPair(Region region) {
        List<SudokuCell> candidateCells = region.findCellsWithCandidateCount(2);
        boolean newNakedPairFound = false;
        for (int i = 0; i < candidateCells.size(); i++) {
            var cell = candidateCells.get(i);
            // Find another cell with same candidates
            for (int j = i + 1; j < candidateCells.size(); j++) {
                var otherCell = candidateCells.get(j);
                // Check for naked pair
                if (isNakedPair(cell, otherCell)) {
                    Set<Integer> candidates = cell.getCandidates();

                    if (isAlreadyProcessed(region, new HashSet<>(List.of(cell, otherCell)))) {
                        continue;
                    }

                    newNakedPairFound = true;
                    Set<SudokuCell> pair = new HashSet<>(List.of(cell, otherCell));
                    region.removeCandidates(candidates, pair);
                    break; // This cell is already processed
                }
            }
        }
        return newNakedPairFound;
    }

    private boolean isNakedPair(SudokuCell cell1, SudokuCell cell2) {
        return cell1.getCandidates().equals(cell2.getCandidates());
    }

    private boolean isAlreadyProcessed(Region region, Set<SudokuCell> pair) {
        return region.findCellsWithCandidates(pair.stream().flatMap(cell -> cell.getCandidates().stream()).collect(Collectors.toSet())).size() == 2;
    }
}