package org.sudokusolver.Strategy.Solver.Regions;

import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Region {

    /**
     * List of cells in the region.
     * Order is maintained for fetching by coordinate.
     */
    private final List<SudokuCell> cells;
    private final Map<SudokuCell, Coordinate> coordinates;

    public Region(List<SudokuCell> cells, Map<SudokuCell, Coordinate> coordinates) {
        this.cells = List.copyOf(cells);
        this.coordinates = Map.copyOf(coordinates);
    }

    /**
     * Gets the list of cells in the region.
     *
     * @return List of Sudoku cells.
     */
    public List<SudokuCell> getCells() {
        return this.cells;
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
     * Gets the board coordinate of a specific cell in the region.
     *
     * @param cell The cell whose coordinate is to be retrieved.
     * @return The coordinate of the cell.
     */
    public Coordinate getCellCoordinate(SudokuCell cell) {
        return this.coordinates.get(cell);
    }

    /**
     * Checks if all cells in the region are solved.
     *
     * @return True if all cells are solved, otherwise false.
     */
    public boolean isSolved() {
        return this.cells.stream().allMatch(SudokuCell::isSolved);
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

    public List<SudokuCell> findCellsWithCandidates(Set<Integer> candidates) {
        return this.cells.stream()
                .filter(sudokuCell -> sudokuCell.getCandidates().containsAll(candidates))
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
     * Removes a candidate from all cells in the region.
     *
     * @param candidate The candidate to remove.
     */
    public void removeCandidateFromAll(int candidate) {
        this.forEach(sudokuCell -> sudokuCell.removeCandidate(candidate));
    }

    /**
     * Removes candidates from all cells in the region except those specified in the exclude set.
     *
     * @param candidates   The candidates to remove.
     * @param excludeCells The cells to exclude.
     */
    public boolean removeCandidates(Set<Integer> candidates, Set<SudokuCell> excludeCells) {
        return this.cells.stream()
                .filter(sudokuCell -> !excludeCells.contains(sudokuCell))
                .map(sudokuCell -> sudokuCell.removeCandidates(candidates))
                .reduce(false, (a, b) -> a || b);
    }

    /**
     * Removes a candidate from all cells in the region except those specified in the exclude set.
     *
     * @param candidate    The candidate to remove.
     * @param excludeCells The cells to exclude.
     */
    public void removeCandidate(int candidate, Set<SudokuCell> excludeCells) {
        this.cells.stream()
                .filter(sudokuCell -> !excludeCells.contains(sudokuCell))
                .forEach(sudokuCell -> sudokuCell.removeCandidate(candidate));
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
