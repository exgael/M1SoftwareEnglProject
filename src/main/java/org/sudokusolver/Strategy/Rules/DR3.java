package org.sudokusolver.Strategy.Rules;

import org.sudokusolver.Gameplay.SudokuCell;
import org.sudokusolver.Strategy.Regions.Region;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.DeductionRule;

import java.util.*;

public class DR3 implements DeductionRule {

    @Override
    public boolean apply(RegionManager regionManager) {
        return regionManager.stream()
                .map(this::applyHiddenPair)
                .toList()
                .contains(true);
    }

    private boolean applyHiddenPair(Region region) {
        List<SudokuCell> unsolvedCells = region.findUnsolvedCells();
        boolean modificationMade = false;

        // Iterate through all possible pairs of candidates
        for (int candidate1 = 1; candidate1 <= 9; candidate1++) {
            for (int candidate2 = candidate1 + 1; candidate2 <= 9; candidate2++) {
                Set<SudokuCell> cellsWithBothCandidates = findCellsWithCandidates(unsolvedCells, candidate1, candidate2);
                if(applyHiddenPair(cellsWithBothCandidates, candidate1, candidate2))  {
                    modificationMade = true;
                }
            }
        }
        return modificationMade;
    }

    private static boolean applyHiddenPair(Set<SudokuCell> cellsWithBothCandidates, int candidate1, int candidate2) {
        if (cellsWithBothCandidates.size() == 2) {
            for (SudokuCell cell : cellsWithBothCandidates) {
                return keepOnlyHiddenPair(candidate1, candidate2, cell);
            }
        }
        return false;
    }

    private static boolean keepOnlyHiddenPair(int candidate1, int candidate2, SudokuCell cell) {
        return  cell.getCandidates().retainAll(Set.of(candidate1, candidate2));
    }

    private Set<SudokuCell> findCellsWithCandidates(List<SudokuCell> cells, int candidate1, int candidate2) {
        Set<SudokuCell> result = new HashSet<>();
        int countCandidate1 = 0;
        int countCandidate2 = 0;

        // First, count occurrences of both candidates across all cells
        for (SudokuCell cell : cells) {
            Set<Integer> candidates = cell.getCandidates();
            if (candidates.contains(candidate1)) {
                countCandidate1++;
            }
            if (candidates.contains(candidate2)) {
                countCandidate2++;
            }

            // Collect cells that contain both candidate1 and candidate2
            if (candidates.containsAll(Set.of(candidate1, candidate2))) {
                result.add(cell);
            }
        }

        // Only valid if both candidates appear exactly twice
        return (countCandidate1 == 2 && countCandidate2 == 2 && result.size() == 2) ? result : Collections.emptySet();
    }
}
