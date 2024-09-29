package org.sudokusolver.Strategy.Solver.Rules;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Regions.Region;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

import java.util.List;

public class DR1 implements DeductionRule {

    @Override
    public boolean apply(RegionManager regionManager) {
        return regionManager.stream()
                .map(region -> applyObviousSingle(region, regionManager))
                .toList()
                .contains(true);
    }

    private boolean applyObviousSingle(Region region, RegionManager regionManager) {
        boolean obviousSingleFound = false;
        List<SudokuCell> candidatesCells = region.findCellsWithCandidateCount(1);
        for (SudokuCell cell : candidatesCells) {
            int value = cell.getCandidates().iterator().next();
            regionManager.setValue(cell.getRow(), cell.getCol(), value);
            obviousSingleFound = true;
        }
        return obviousSingleFound;
    }
}