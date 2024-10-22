package org.sudokusolver.Strategy.Sudoku.Regions;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
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
     * Get the list of cell's value in the region
     * @return List of values.
     */
    public List<Integer> getValues() {
        return cells.stream().map(SudokuCell::getValue).toList();
    }

    /**
     * Checks if the region contains a specific value.
     *
     * @param value The value to check for.
     * @return True if the region contains the value, otherwise false.
     */
    public boolean containsValue(int value) {
        return this.cells.stream()
                .anyMatch(sudokuCell -> sudokuCell.getValue() == value);
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

    /**
     * Performs an action on each cell in the region.
     *
     * @param action The action to perform on each cell.
     */
    public void forEach(Consumer<SudokuCell> action) {
        this.cells.forEach(action);
    }
}
