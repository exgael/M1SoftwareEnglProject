package org.sudokusolver.Strategy.Solver.Rules;


import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Regions.Region;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DR2 implements DeductionRule {

    @Override
    public boolean apply(RegionManager regionManager, Sudoku sudoku) {
        return regionManager.stream()
                .map(region -> applyHiddenSingle(region, sudoku))
                .toList()
                .contains(true);
    }

    private boolean applyHiddenSingle(Region region, Sudoku sudoku) {
        List<SudokuCell> cells = region.cells();
        Map<Integer, SudokuCell> candidateMap = new HashMap<>();
        boolean hiddenSingleFound = false;
        for (SudokuCell cell : cells) {
            if (cell.getValue() == 0) {
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
                sudoku.setValue(cell.getRow(), cell.getCol(), entry.getKey());
                hiddenSingleFound = true;
            }
        }
        return hiddenSingleFound;
    }
}