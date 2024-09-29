package org.sudokusolver.Strategy.Solver.Regions;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.*;
import java.util.function.Consumer;
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
            List<SudokuCell> subgridCells = handleGetCellsInSubgrid(sudoku, startRow, startCol);

            // Coordinates
            Map<SudokuCell, Coordinate> rowHashMapCoordinates = buildCoordinateMap(rowCells, getRowCoordinates(i));
            Map<SudokuCell, Coordinate> columnHashMapCoordinates = buildCoordinateMap(columnCells, getColumnCoordinates(i));
            Map<SudokuCell, Coordinate> subgridHashMapCoordinates = buildCoordinateMap(subgridCells, getSubgridCoordinates(startRow, startCol));

            // Add the initialized regions
            this.rows.add(new Region(rowCells, rowHashMapCoordinates));
            this.columns.add(new Region(columnCells, columnHashMapCoordinates));
            this.subgrids.add(new Region(subgridCells, subgridHashMapCoordinates));
        }
    }

    /**
     * Builds a map of cells and coordinates.
     *
     * @param cells       The list of cells.
     * @param coordinates The list of coordinates.
     * @return The map of cells and coordinates.
     */
    private Map<SudokuCell, Coordinate> buildCoordinateMap(List<SudokuCell> cells, List<Coordinate> coordinates) {
        Map<SudokuCell, Coordinate> map = new HashMap<>();
        for (int j = 0; j < cells.size(); j++) {
            map.put(cells.get(j), coordinates.get(j));
        }
        return map;
    }

    /**
     * Returns the integer from the board.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The cell.
     */
    public int getValue(int row, int col) {
        return getRowRegion(row).getCells().get(col).getValue();
    }

    public SudokuCell getCell(int row, int col) {
        return getRowRegion(row).getCells().get(col);
    }

    public void setValue(int row, int col, int value) {
        // Check if the value can be placed in the cell
        if (canPlaceValue(row, col, value)) {
            // Set the value in the specific cell
            SudokuCell cell = getRowRegion(row).getCells().get(col);
            cell.setValue(value);
            System.out.println("Set from RegionManager: " + value);
            cell.clearCandidates();  // Clear candidates for the cell where we set the value

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

    /**
     * Returns a list of possible values for a cell.
     *
     * @param row The row index.
     * @param col The column index.
     * @return List of possible values.
     */
    public List<Integer> getPossibleValues(int row, int col) {
        // If the cell is already solved, return an empty list
        if (getValue(row, col) != DEFAULT_VALUE) {
            return new ArrayList<>();
        }

        // Return a list of candidates, get cell, return candidates
        return getRowRegion(row).getCells().get(col).getCandidates().stream().toList();
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
        boolean isCellEmpty = getValue(row, col) == DEFAULT_VALUE;
        boolean isValueNotInTargetedRegions = !isValueInTargetedRegions(row, col, value);
        return isCellEmpty && isValueNotInTargetedRegions;
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

    /**
     * Iterates over all regions.
     *
     * @param action The action to be performed.
     */
    public void forEachRegion(Consumer<Region> action) {
        rows.forEach(action);
        columns.forEach(action);
        subgrids.forEach(action);
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
    private List<SudokuCell> handleGetCellsInSubgrid(Sudoku board, int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        iterateSubgrid(startRow, startCol, coordinate ->
                cells.add(board.getElement(coordinate.row(), coordinate.column()))
        );
        return cells;
    }

    /**
     * Returns a list of coordinates for a given row.
     *
     * @param row The row index.
     * @return List of coordinates.
     */
    private List<Coordinate> getRowCoordinates(int row) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(col -> new Coordinate(row, col))
                .toList();
    }

    /**
     * Returns a list of coordinates for a given column.
     *
     * @param col The column index.
     * @return List of coordinates.
     */
    private List<Coordinate> getColumnCoordinates(int col) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(row -> new Coordinate(row, col))
                .toList();
    }

    /**
     * Returns a list of coordinates for a subgrid.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @return List of coordinates.
     */
    private List<Coordinate> getSubgridCoordinates(int startRow, int startCol) {
        List<Coordinate> coordinates = new ArrayList<>();
        iterateSubgrid(startRow, startCol, coordinates::add);
        return coordinates;
    }

    /**
     * Executes an action for each cell in a subgrid.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @param action   The action to be performed.
     */
    private void iterateSubgrid(int startRow, int startCol, Consumer<Coordinate> action) {
        for (int rOffset = 0; rOffset < subgridSize; rOffset++) {
            for (int cOffset = 0; cOffset < subgridSize; cOffset++) {
                action.accept(new Coordinate(startRow + rOffset, startCol + cOffset));
            }
        }
    }
}
