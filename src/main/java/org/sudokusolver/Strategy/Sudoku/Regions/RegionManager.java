package org.sudokusolver.Strategy.Sudoku.Regions;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegionManager {

    private final List<Region> rows;
    private final List<Region> columns;
    private final List<Region> subgrids;

    private final int sudokuSize;
    private final int subgridSize;
    private final int DEFAULT_VALUE = 0;

    public RegionManager(Sudoku sudoku) {
        this.sudokuSize = sudoku.getBoardSize();
        this.subgridSize = sudoku.getSubgridSize();
        this.rows = new ArrayList<>(sudokuSize);
        this.columns = new ArrayList<>(sudokuSize);
        this.subgrids = new ArrayList<>(sudokuSize);
        initializeRegions(sudoku);
    }

    /**
     * Initializes the regions of the Sudoku board.
     *
     * @param sudoku The Sudoku board.
     */
    private void initializeRegions(Sudoku sudoku) {
        for (int i = 0; i < sudokuSize; i++) {
            // Collections of cells
            List<SudokuCell> rowCells = getRowCells(sudoku, i);
            List<SudokuCell> columnCells = getColumnCells(sudoku, i);
            int startRow = (i / subgridSize) * subgridSize;
            int startCol = (i % subgridSize) * subgridSize;
            List<SudokuCell> subgridCells = getCellsInSubgrid(sudoku, startRow, startCol);

            // Add the initialized regions
            this.rows.add(new Region(rowCells));
            this.columns.add(new Region(columnCells));
            this.subgrids.add(new Region(subgridCells));
        }
    }

    public void setValue(int row, int col, int value) {
        // Check if the value can be placed in the cell
        if (canPlaceValue(row, col, value)) {
            // Set the value in the specific cell
            SudokuCell cell = getRowRegion(row).cells().get(col);
            cell.setValue(value);

            // Remove the value from the candidates of related cells in the same row, column, and subgrid
            removeCandidateFromRelatedRegions(row, col, value);
        }
    }

    /**
     * Removes a value from the candidates of related cells in the same row, column, and subgrid.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param value The value to be removed.
     */
    private void removeCandidateFromRelatedRegions(int row, int col, int value) {

        // Remove from the same row
        getRowRegion(row).forEach(cell -> cell.removeCandidate(value));

        // Remove from the same column
        getColumnRegion(col).forEach(cell -> cell.removeCandidate(value));

        // Remove from the same subgrid
        getSubgridRegion(row, col).forEach(cell -> cell.removeCandidate(value));
    }

    /**
     * Return a stream of all the regions.
     *
     * @return The stream of regions.
     */
    public Stream<Region> stream() {
        return Stream.of(rows, columns, subgrids).flatMap(Collection::stream);
    }

    public List<Integer> getPossibleValues(int row, int col) {
        Set<Integer> possibleValues = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toSet());
        removeValuesFromSet(possibleValues, getRowRegion(row).getValues());
        removeValuesFromSet(possibleValues, getColumnRegion(col).getValues());
        removeValuesFromSet(possibleValues, getSubgridRegion(row, col).getValues());
        return new ArrayList<>(possibleValues);
    }

    private void removeValuesFromSet(Set<Integer> set, List<Integer> values) {
        values.forEach(set::remove);
    }

    /**
     * Checks if a value can be placed in a cell.
     * To do so, the same value must not be present in the same row, column, or subgrid.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param value The value to be checked.
     * @return True if the value can be placed, false otherwise.
     */
    public boolean canPlaceValue(int row, int col, int value) {
        return !isValueInTargetedRegions(row, col, value);
    }

    /**
     * Checks if a value is in the targeted regions.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param value The value to be checked.
     * @return True if the value is in the targeted regions, false otherwise.
     */
    private boolean isValueInTargetedRegions(int row, int col, int value) {
        Set<Region> regions = new HashSet<>();
        regions.add(this.getRowRegion(row));
        regions.add(this.getColumnRegion(col));
        regions.add(this.getSubgridRegion(row, col));
        return regions.stream().anyMatch(region -> region.containsValue(value));
    }


    // /////////////////////////////////////////
    //             Region accessors           //
    // /////////////////////////////////////////

    /**
     * Returns a row region.
     *
     * @param row The row index.
     * @return The row region.
     */
    public Region getRowRegion(int row) {
        return rows.get(row);
    }

    /**
     * Returns a row region.
     *
     * @param col The column index.
     * @return The row region.
     */
    public Region getColumnRegion(int col) {
        return columns.get(col);
    }

    /**
     * Returns a subgrid region.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The subgrid region.
     */
    public Region getSubgridRegion(int row, int col) {
        int gridRow = row / subgridSize;
        int gridCol = col / subgridSize;
        int index = gridRow * subgridSize + gridCol;
        return getSubgridRegion(index);
    }

    /**
     * Returns a subgrid region.
     *
     * @param index The index of the subgrid.
     * @return The subgrid region.
     */
    public Region getSubgridRegion(int index) {
        return subgrids.get(index);
    }

    // /////////////////////////////////////////
    // Helper methods to initialize regions   //
    // /////////////////////////////////////////

    /**
     * Returns a list of cells in a row.
     *
     * @param board The Sudoku board.
     * @param row   The row index.
     * @return List of cells.
     */
    private List<SudokuCell> getRowCells(Sudoku board, int row) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(col -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a column.
     *
     * @param board The Sudoku board.
     * @param col   The column index.
     * @return List of cells.
     */
    private List<SudokuCell> getColumnCells(Sudoku board, int col) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(row -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a subgrid.
     *
     * @param board    The Sudoku board.
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @return List of cells.
     */
    private List<SudokuCell> getCellsInSubgrid(Sudoku board, int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        iterateSubgrid(startRow, startCol, (r, c) ->
                cells.add(board.getElement(r, c))
        );
        return cells;
    }

    /**
     * Executes an action for each cell in a subgrid.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @param action   The action to be performed.
     */
    private void iterateSubgrid(int startRow, int startCol, BiConsumer<Integer, Integer> action) {
        for (int rOffset = 0; rOffset < subgridSize; rOffset++) {
            for (int cOffset = 0; cOffset < subgridSize; cOffset++) {
                action.accept(startRow + rOffset, startCol + cOffset);
            }
        }
    }
}
