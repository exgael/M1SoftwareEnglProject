package org.sudokusolver.Core;

import org.sudokusolver.Core.Regions.Region;
import org.sudokusolver.Core.Regions.RegionType;
import org.sudokusolver.Utils.Inspectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class SudokuBoard extends ObservableBoard<SudokuCell> implements Inspectable {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private static final int DEFAULT_VALUE = 0;

    private final List<Region> rows = new ArrayList<>();
    private final List<Region> columns = new ArrayList<>();
    private final List<Region> subgrids = new ArrayList<>();

    public SudokuBoard() {
        super(BOARD_SIZE, BOARD_SIZE, SudokuCell::new);
        initializeRegions();
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

    public boolean isSolved() {
        return rows.stream().allMatch(Region::isSolved);
    }

    private void validateValue(int value) {
        if (value == DEFAULT_VALUE) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public List<Integer> getPossibleValues(int row, int col) {
        if (getValue(row, col) != DEFAULT_VALUE) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, BOARD_SIZE)
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

    public void forEachRegion(Consumer<Region> action) {
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

    public Region getRowRegion(int row) {
        return rows.get(row);
    }

    public Region getColumnRegion(int col) {
        return columns.get(col);
    }

    public Region getSubgridRegion(int row, int col) {
        int gridRow = row / SUBGRID_SIZE;
        int gridCol = col / SUBGRID_SIZE;
        int index = gridRow * SUBGRID_SIZE + gridCol;
        return getSubgridRegion(index);
    }

    private Region getSubgridRegion(int index) {
        return subgrids.get(index);
    }

    // Helper methods to initialize regions

    private List<SudokuCell> getColumnCells(int col) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(row -> getElement(row, col))
                .toList();
    }

    private List<SudokuCell> getRowCells(int row) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(col -> getElement(row, col))
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

    @Override
    public String debugDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("SudokuBoard{\n");

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                sb.append(getValue(row, col));
                sb.append(" ");

                if ((col + 1) % SUBGRID_SIZE == 0 && col + 1 < BOARD_SIZE) {
                    sb.append("| ");
                }
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
