package org.sudokusolver.Strategy.Rules;

import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Gameplay.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.DeductionRule;
import org.sudokusolver.Gameplay.Sudoku.Regions.Region;

import java.util.List;

public class DR1 implements DeductionRule {

    @Override
    public boolean apply(Solvable sudoku) {
        return sudoku.streamRegions()
                .map(region -> applyObviousSingle(region, sudoku))
                .toList()
                .contains(true);
    }

    private boolean applyObviousSingle(Region region, Solvable sudoku) {
        boolean obviousSingleFound = false;
        List<SudokuCell> candidatesCells = region.findCellsWithCandidateCount(1);
        for (SudokuCell cell : candidatesCells) {
            int value = cell.getCandidates().iterator().next();
            sudoku.setValue(cell.getRow(), cell.getCol(), value);
            obviousSingleFound = true;
        }
        return obviousSingleFound;
    }
}