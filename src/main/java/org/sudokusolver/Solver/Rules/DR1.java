package org.sudokusolver.Solver.Rules;

import org.sudokusolver.Solver.Regions.Region;
import org.sudokusolver.Solver.Regions.RegionManager;
import org.sudokusolver.Solver.Solvers.DeductionRule;
import org.sudokusolver.Core.SudokuCell;

import java.util.List;
import java.util.Set;

public class DR1 implements DeductionRule {

    @Override
    public void apply(RegionManager regionManager) {
        regionManager.forEachRegion(this::applyObviousSingle);
    }

    private void applyObviousSingle(Region region) {
        List<SudokuCell> candidatesCells = region.findCellsWithCandidateCount(1);
        for (SudokuCell cell : candidatesCells) {
            Set<Integer> candidates = cell.getCandidates();
            cell.setNumber(candidates.iterator().next());
        }
    }
}