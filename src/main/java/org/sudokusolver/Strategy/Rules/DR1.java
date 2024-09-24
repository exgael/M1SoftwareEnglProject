package org.sudokusolver.Strategy.Rules;

import org.sudokusolver.Gameplay.SudokuCell;
import org.sudokusolver.Strategy.Regions.Coordinate;
import org.sudokusolver.Strategy.Regions.Region;
import org.sudokusolver.Strategy.Regions.RegionManager;
import org.sudokusolver.Strategy.DeductionRule;

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
            Coordinate coordinate = region.getCellCoordinate(cell);
            regionManager.setValue(coordinate.row(), coordinate.column(), value);
            obviousSingleFound = true;
        }
        return obviousSingleFound;
    }
}