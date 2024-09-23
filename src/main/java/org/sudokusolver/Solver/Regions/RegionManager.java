package org.sudokusolver.Solver.Regions;

import org.sudokusolver.Core.SudokuBoard;
import org.sudokusolver.Core.SudokuCell;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class RegionManager {

    private final List<Region> rows;
    private final List<Region> columns;
    private final List<Region> subgrids;

    private final int sudokuSize;
    private final int subgridSize;
    private final int DEFAULT_VALUE = 0;

    public RegionManager(SudokuBoard sudokuBoard) {
        this.sudokuSize = sudokuBoard.getBoardSize();
        this.subgridSize = sudokuBoard.getSubgridSize();
        this.rows = new ArrayList<>(sudokuSize);
        this.columns = new ArrayList<>(sudokuSize);
        this.subgrids = new ArrayList<>(sudokuSize);
        initializeRegions(sudokuBoard);
    }

    private void initializeRegions(SudokuBoard sudokuBoard) {
        for (int i = 0; i < sudokuSize; i++) {
            // Cells
            List<SudokuCell> rowCells = getRowCells(sudokuBoard, i);
            List<SudokuCell> columnCells = getColumnCells(sudokuBoard, i);
            int startRow = (i / subgridSize) * subgridSize;
            int startCol = (i % subgridSize) * subgridSize;
            List<SudokuCell> subgridCells = handleGetCellsInSubgrid(sudokuBoard, startRow, startCol);

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

    private Map<SudokuCell, Coordinate> buildCoordinateMap(List<SudokuCell> cells, List<Coordinate> coordinates) {
        Map<SudokuCell, Coordinate> map = new HashMap<>();
        for (int j = 0; j < cells.size(); j++) {
            map.put(cells.get(j), coordinates.get(j));
        }
        return map;
    }

    public int getValue(int row, int col) {
        return getRowRegion(row).getCells().get(col).getNumber();
    }

    public void setValue(int row, int col, int value) {
        if (canPlaceValue(row, col, value)) {
            // Set the value in the specific cell
            SudokuCell cell = getRowRegion(row).getCells().get(col);
            cell.setNumber(value);

            // Remove the value from the candidates of related cells in the same row, column, and subgrid
            removeCandidatesFromRelatedRegions(row, col, value);
        }
    }

    private void removeCandidatesFromRelatedRegions(int row, int col, int value) {
        // Remove from the same row
        Region rowRegion = getRowRegion(row);
        rowRegion.forEach(sudokuCell -> {
            if (!sudokuCell.isSolved()) {
                sudokuCell.removeCandidate(value);
            }
        });

        // Remove from the same column
        Region columnRegion = getColumnRegion(col);
        columnRegion.forEach(sudokuCell -> {
            if (!sudokuCell.isSolved()) {
                sudokuCell.removeCandidate(value);
            }
        });

        // Remove from the same subgrid
        Region subgridRegion = getSubgridRegion(row, col);
        subgridRegion.forEach(sudokuCell -> {
            if (!sudokuCell.isSolved()) {
                sudokuCell.removeCandidate(value);
            }
        });
    }

    public List<Integer> getPossibleValues(int row, int col) {
        if (getValue(row, col) != DEFAULT_VALUE) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, sudokuSize)
                .filter(value -> canPlaceValue(row, col, value))
                .boxed()
                .toList();
    }

    public boolean canPlaceValue(int row, int col, int value) {
        return getValue(row, col) == DEFAULT_VALUE && !isValueInTargetedRegions(row, col, value);
    }

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
     * @param row The row index.
     * @return The row region.
     */
    public Region getRowRegion(int row) {
        return rows.get(row);
    }

    /**
     * Returns a row region.
     * @param col The column index.
     * @return The row region.
     */
    public Region getColumnRegion(int col) {
        return columns.get(col);
    }

    /**
     * Returns a subgrid region.
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
     * @param index The index of the subgrid.
     * @return The subgrid region.
     */
    public Region getSubgridRegion(int index) {
        return subgrids.get(index);
    }

    /**
     * Iterates over all regions.
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
     * @param board The Sudoku board.
     * @param row The row index.
     * @return List of cells.
     */
    private List<SudokuCell> getRowCells(SudokuBoard board, int row) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(col -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a column.
     * @param board The Sudoku board.
     * @param col The column index.
     * @return List of cells.
     */
    private List<SudokuCell> getColumnCells(SudokuBoard board, int col) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(row -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a subgrid.
     * @param board The Sudoku board.
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @return List of cells.
     */
    private List<SudokuCell> handleGetCellsInSubgrid(SudokuBoard board, int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        for (int r = 0; r < subgridSize; r++) {
            for (int c = 0; c < subgridSize; c++) {
                cells.add(board.getElement(startRow + r, startCol + c));
            }
        }
        return cells;
    }

    /**
     * Returns a list of coordinates for a given row.
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
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @return List of coordinates.
     */
    private List<Coordinate> getSubgridCoordinates(int startRow, int startCol) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int r = 0; r < subgridSize; r++) {
            for (int c = 0; c < subgridSize; c++) {
                coordinates.add(new Coordinate(startRow + r, startCol + c));
            }
        }
        return coordinates;
    }
}
