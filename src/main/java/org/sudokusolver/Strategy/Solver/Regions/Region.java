package org.sudokusolver.Strategy.Solver.Regions;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @param cells
List of cells in the region.
Order is maintained for fetching by coordinate. */
public record Region(List<SudokuCell> cells) {

    public Region(List<SudokuCell> cells) {
        this.cells = List.copyOf(cells);
    }

    /**
     * Gets the list of cells in the region.
     *
     * @return List of Sudoku cells.
     */
    @Override public List<SudokuCell> cells() {
        return this.cells;
    }

    /**
     * Finds cells with a specific candidate count.
     *
     * @param candidateCount The number of candidates a cell should have.
     * @return List of cells with the specific candidate count.
     */
    public List<SudokuCell> findCellsWithCandidateCount(int candidateCount) {
        return this.cells.stream()
                .filter(sudokuCell -> sudokuCell.candidateCount() == candidateCount)
                .toList();
    }

    /**
     * Finds unsolved cells in the region.
     *
     * @return Set of unsolved cells.
     */
    public Set<SudokuCell> findUnsolvedCells() {
        return this.cells.stream()
                .filter(sudokuCell -> !sudokuCell.isSolved())
                .collect(Collectors.toSet());
    }
}
