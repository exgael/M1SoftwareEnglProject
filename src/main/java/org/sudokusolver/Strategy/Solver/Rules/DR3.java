package org.sudokusolver.Strategy.Solver.Rules;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuCell;
import org.sudokusolver.Strategy.Solver.DeductionRule;
import org.sudokusolver.Strategy.Solver.Regions.Region;
import org.sudokusolver.Strategy.Solver.Regions.RegionManager;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DR3 implements DeductionRule {

    @NotNull
    private static Set<Integer> extractAllCandidates(Set<SudokuCell> unsolvedCells) {
        return unsolvedCells.stream()
                .flatMap(cell -> cell.getCandidates().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean apply(RegionManager regionManager, Sudoku sudoku) {
        return regionManager.stream()
                .map(this::applyHiddenPairToRegion)
                .toList()
                .contains(true);
    }

    /**
     * Applies the hidden pair deduction rule to a region.
     *
     * @param region The region to apply the hidden pair deduction rule to.
     * @return True if a modification was made, otherwise false.
     */
    private boolean applyHiddenPairToRegion(Region region) {
        Set<SudokuCell> unsolvedCells = region.findUnsolvedCells();
        Set<Integer> uniqueCandidates = extractAllCandidates(unsolvedCells);

        boolean modificationMade = false;

        // Iterate through all possible pairs of unique candidates
        for (var candidate1 : uniqueCandidates) {
            for (var candidate2 : uniqueCandidates) {
                if (candidate1 < candidate2) {
                    // Find cells that contain both candidate1 and candidate2
                    Set<SudokuCell> cellsWithBothCandidates = findCellsWithCandidates(unsolvedCells, candidate1, candidate2);

                    // Apply hidden pair to the cells
                    if (!cellsWithBothCandidates.isEmpty() && applyHiddenPairToCells(cellsWithBothCandidates, candidate1, candidate2)) {
                        modificationMade = true;
                    }
                }
            }
        }
        return modificationMade;
    }

    /**
     * Finds two cells that contain both candidate1 and candidate2.
     * If any other cells contain candidate1 or candidate2, the method returns an empty set.
     *
     * @param cells      The cells to search through.
     * @param candidate1 The first candidate to search for.
     * @param candidate2 The second candidate to search for.
     * @return if found, a set containing the two cells that contain both candidate1 and candidate2, otherwise an empty set.
     */
    private Set<SudokuCell> findCellsWithCandidates(Set<SudokuCell> cells, int candidate1, int candidate2) {
        // Find cells that contain both candidate1 and candidate2
        Set<SudokuCell> intersection = cells.stream()
                .filter(cell -> cell.hasCandidate(candidate1) && cell.hasCandidate(candidate2))
                .collect(Collectors.toSet());

        // Ensure the intersection contains exactly two cells
        if (!(intersection.size() == 2)) {
            return Collections.emptySet();
        }

        // Ensure that candidate1 and candidate2 do not appear in other cells
        boolean candidatesAppearElsewhere = cells.stream()
                .filter(cell -> !intersection.contains(cell))  // Only consider cells not in the intersection
                .anyMatch(cell -> cell.hasCandidate(candidate1) || cell.hasCandidate(candidate2));

        return candidatesAppearElsewhere ? Collections.emptySet() : intersection;
    }

    /**
     * Applies the hidden pair to the cells.
     *
     * @param cellsWithBothCandidates The cells to apply the hidden pair to.
     * @param candidate1              The first candidate of the hidden pair.
     * @param candidate2              The second candidate of the hidden pair.
     * @return True if a modification was made, otherwise false.
     */
    private static boolean applyHiddenPairToCells(Set<SudokuCell> cellsWithBothCandidates, int candidate1, int candidate2) {
        boolean modificationMade = false;
        for (SudokuCell cell : cellsWithBothCandidates) {
            if (retainOnlyHiddenPair(candidate1, candidate2, cell)) {
                modificationMade = true;
            }
        }
        return modificationMade;
    }

    /**
     * Retains only the hidden pair in the cell's candidates.
     * A hidden pair can be found, but it is not guaranteed that the cell will be modified.
     * The cell may already have the hidden pair as its candidates.
     *
     * @param candidate1 The first candidate of the hidden pair.
     * @param candidate2 The second candidate of the hidden pair.
     * @param cell       The cell to retain the hidden pair in.
     * @return True if the cell was modified, otherwise false.
     */
    private static boolean retainOnlyHiddenPair(int candidate1, int candidate2, SudokuCell cell) {
        return cell.getCandidates().retainAll(Set.of(candidate1, candidate2));
    }
}
