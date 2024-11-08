package org.sudokusolver.Strategy.Rules;


import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Gameplay.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Gameplay.Sudoku.Regions.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DR2 implements DeductionRule {

    @Override
    public boolean apply(Solvable sudoku) {
        return sudoku.streamRegions()
                .map(region -> applyHiddenSingle(region, sudoku))
                .toList()
                .contains(true);
    }

    private boolean applyHiddenSingle(Region region, Solvable sudoku) {
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