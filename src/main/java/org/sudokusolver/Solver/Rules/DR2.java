package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Regions.Coordinate;
import org.sudokusolver.Solver.Regions.Region;
import org.sudokusolver.Solver.Regions.RegionManager;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.*;

public class DR2 implements DeductionRule {

    @Override
    public boolean apply(RegionManager regionManager) {
        return regionManager.stream()
                .map(this::applyNakedPair)
                .toList()
                .contains(true);
    }

    private boolean applyNakedPair(Region region) {
        boolean nakedPairFound = false;
        List<SudokuCell> pairCells = region.findCellsWithCandidateCount(2);
        for (int i = 0; i < pairCells.size(); i++) {
            for (int j = i + 1; j < pairCells.size(); j++) {
                Set<Integer> candidates = new HashSet<>(pairCells.get(i).getCandidates());
                if (candidates.equals(new HashSet<>(pairCells.get(j).getCandidates()))) {
                    removeCandidatesFromOtherCells(region, candidates, Arrays.asList(pairCells.get(i), pairCells.get(j)));
                    nakedPairFound = true;
                }
            }
        }
        return nakedPairFound;
    }

    private void applyHiddenSingle(Region region, RegionManager regionManager) {
        List<SudokuCell> cells = region.getCells();
        Map<Integer, SudokuCell> candidateMap = new HashMap<>();

        for (SudokuCell cell : cells) {
            if (cell.getNumber() == 0) {
                for (int candidate : cell.getCandidates()) {
                    if (!candidateMap.containsKey(candidate)) {
                        candidateMap.put(candidate, cell);
                    } else {
                        // if number not unique in region, we remove it (not hidden single)
                        candidateMap.put(candidate, null);
                    }
                }
            }
        }

        for (Map.Entry<Integer, SudokuCell> entry : candidateMap.entrySet()) {
            SudokuCell cell = entry.getValue();
            if (cell != null) {
                Coordinate coordinate = region.getCellCoordinate(cell);
                regionManager.setValue(coordinate.row(), coordinate.column(), entry.getKey());
            }
        }
    }

    private void removeCandidatesFromOtherCells(Region region, Set<Integer> candidates, List<SudokuCell> excludeCells) {
        region.findUnsolvedCells().stream()
                .filter(cell -> !excludeCells.contains(cell))
                .forEach(cell -> cell.getCandidates().removeAll(candidates));
    }
}