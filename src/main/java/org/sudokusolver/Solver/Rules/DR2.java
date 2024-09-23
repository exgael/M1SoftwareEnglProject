package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Core.SudokuCell;
import org.sudokusolver.Solver.Regions.Region;
import org.sudokusolver.Solver.Regions.RegionManager;
import org.sudokusolver.Solver.Solvers.DeductionRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DR2 implements DeductionRule {

    @Override
    public void apply(RegionManager regionManager) {
        regionManager.forEachRegion(this::applyHiddenSingle);
    }

    private void applyHiddenSingle(Region region) {
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
                cell.setNumber(entry.getKey());
            }
        }
    }
}