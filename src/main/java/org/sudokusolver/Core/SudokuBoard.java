package org.sudokusolver.Core;

import org.sudokusolver.Core.SudokuComponents.Regions.Region;
import org.sudokusolver.Core.SudokuComponents.Regions.RegionType;
import org.sudokusolver.Core.SudokuComponents.SudokuCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class SudokuBoard extends ObservableBoard<SudokuCell> {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private final List<Region> rows = new ArrayList<>();
    private final List<Region> columns = new ArrayList<>();
    private final List<Region> subgrids = new ArrayList<>();

    public SudokuBoard() {
        super(9, 9, SudokuCell::new);
        initializeCells();
        initializeRegions();
    }

    private void initializeCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                getElement(row, col).initializeCandidates(BOARD_SIZE);
            }
        }
    }

    private void initializeRegions() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<SudokuCell> rowCells = getRowCells(i);
            List<SudokuCell> columnCells = getColumnCells(i);
            int startRow = (i / SUBGRID_SIZE) * SUBGRID_SIZE;
            int startCol = (i % SUBGRID_SIZE) * SUBGRID_SIZE;
            List<SudokuCell> subgridCells = handleGetCellsInSubgrid(startRow, startCol);

            this.rows.add(new Region(rowCells));
            this.columns.add(new Region(columnCells));
            this.subgrids.add(new Region(subgridCells));
        }
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getValue(int row, int col) {
        return getElement(row, col).getNumber();
    }

    public void setValue(int row, int col, int value) {
        validateValue(value);
        getElement(row, col).setNumber(value);

        // Remove value from candidates in related regions
        getRowRegion(row).removeCandidate(value);
        getColumnRegion(col).removeCandidate(value);
        getSubgridRegion(row, col).removeCandidate(value);
    }

    private void validateValue(int value) {
        if (value == 0) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public List<Integer> getPossibleValues(int row, int col) {
        if (getValue(row, col) != 0) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, BOARD_SIZE)
                .filter(value -> canPlaceValue(row, col, value))
                .boxed()
                .toList();
    }

    private boolean canPlaceValue(int row, int col, int value) {
        return getValue(row, col) == 0 && !isValueInTargetedRegions(row, col, value);
    }

    private boolean isValueInTargetedRegions(int row, int col, int value) {
        Set<Region> regions = new HashSet<>();
        regions.add(this.getRowRegion(row));
        regions.add(this.getColumnRegion(col));
        regions.add(this.getSubgridRegion(row, col));
        return regions.stream().anyMatch(region -> region.containsValue(value));
    }

    public void applyToAllRegions(Consumer<Region> action) {
        rows.forEach(action);
        columns.forEach(action);
        subgrids.forEach(action);
    }

    // Region accessors

    private Region getRegion(int index, RegionType regionType) {
        return switch (regionType) {
            case ROW -> getRowRegion(index);
            case COLUMN -> getColumnRegion(index);
            case SUBGRID -> getSubgridRegion(index);
        };
    }

    private Region getRowRegion(int row) {
        return rows.get(row);
    }

    private Region getColumnRegion(int col) {
        return columns.get(col);
    }

    private Region getSubgridRegion(int index) {
        return subgrids.get(index);
    }

    // Helper methods to initialize regions

    private List<SudokuCell> getColumnCells(int row) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(col -> getElement(row, col))
                .toList();
    }

    private List<SudokuCell> getRowCells(int col) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(row -> getElement(row, col))
                .toList();
    }

    private List<SudokuCell> handleGetCellsInSubgrid(int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                cells.add(getElement(startRow + r, startCol + c));
            }
        }
        return cells;
    }

    private Region getSubgridRegion(int row, int col) {
        int gridRow = row / SUBGRID_SIZE;
        int gridCol = col / SUBGRID_SIZE;
        int index = gridRow * SUBGRID_SIZE + gridCol;
        return subgrids.get(index);
    }
}
