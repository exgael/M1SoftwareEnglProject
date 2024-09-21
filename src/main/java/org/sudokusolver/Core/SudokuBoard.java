package org.sudokusolver.Core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class SudokuBoard extends ObservableBoard<SudokuCell> {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    public SudokuBoard() {
        super(9, 9, SudokuCell::new);
        initializeCells();
    }

    private void initializeCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                getElement(row, col).initializeCandidates(BOARD_SIZE);
            }
        }
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getSubgridSize() {
        return SUBGRID_SIZE;
    }

    public int getValue(int row, int col) {
        return getElement(row, col).getNumber();
    }

    public void setValue(int row, int col, int value) {
        validateValue(value);
        getElement(row, col).setNumber(value);
        this.removeRelatedCandidates(row, col, value);
    }

    private void removeRelatedCandidates(int row, int col, int candidate) {
        this.forEachRegionRelatedToCoordinates(row, col, sudokuCell -> sudokuCell.removeCandidate(candidate));
    }

    private void validateValue(int value) {
        if (value == 0) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void clearValue(int row, int col) {
        getElement(row, col).setNumber(0);
    }

    public boolean isBoardSolved() {
        return false;
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
        return getValue(row, col) == 0 &&
                !isValueInAnyRegionRelatedToCoordinates(row, col, value);
    }

    private boolean isValueInAnyRegionRelatedToCoordinates(int row, int col, int value) {
        return this.getCellsFromRegionsRelatedToCoordinates(row, col)
                .stream()
                .anyMatch(sudokuCell -> sudokuCell.getNumber() == value);
    }

    private Set<SudokuCell> getCellsFromRegionsRelatedToCoordinates(int row, int col) {
        Set<SudokuCell> cells = new HashSet<>();
        cells.addAll(this.getCellsInRow(row));
        cells.addAll(this.getCellsInColumn(col));
        cells.addAll(this.getCellsInSubgrid(row, col));
        return cells;
    }

    public Set<Integer> getCandidates(int row, int col) {
        return getElement(row, col).getCandidates();
    }

    public void removeCandidate(int row, int col, int candidate) {
        getElement(row, col).removeCandidate(candidate);
    }

    public boolean hasCandidate(int row, int col, int candidate) {
        return getElement(row, col).hasCandidate(candidate);
    }

    public boolean isCellSolved(int row, int col) {
        return getElement(row, col).isSolved();
    }

    public int candidateCount(int row, int col) {
        return getElement(row, col).candidateCount();
    }

    public List<SudokuCell> findCellsWithCandidateCountInRegion(int index, int count, RegionType regionType) {
        return this.getCellsInRegion(index, regionType)
                .stream()
                .filter(sudokuCell -> sudokuCell.candidateCount() == count)
                .toList();
    }

    public List<SudokuCell> findUnsolvedCellsInRegion(int index, RegionType regionType) {
        return this.getCellsInRegion(index, regionType)
                .stream()
                .filter(sudokuCell -> !sudokuCell.isSolved())
                .toList();
    }

    // REGIONS GETTERS

    private List<SudokuCell> getCellsInRegion(int index, RegionType regionType) {
        return switch (regionType) {
            case ROW -> getCellsInRow(index);
            case COLUMN -> getCellsInColumn(index);
            case SUBGRID -> getCellsInSubgridByIndex(index);
        };
    }

    private List<SudokuCell> getCellsInRow(int row) {
        List<SudokuCell> cells = new ArrayList<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            cells.add(getElement(row, col));
        }
        return cells;
    }

    private List<SudokuCell> getCellsInColumn(int col) {
        List<SudokuCell> cells = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            cells.add(getElement(row, col));
        }
        return cells;
    }

    private List<SudokuCell> getCellsInSubgrid(int row, int col) {
        int startRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        return handleGetCellsInSubgrid(startRow, startCol);
    }

    private List<SudokuCell> getCellsInSubgridByIndex(int index) {
        int startRow = (index / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (index % SUBGRID_SIZE) * SUBGRID_SIZE;
        return handleGetCellsInSubgrid(startRow, startCol);
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

    // FOR EACH

    private void forEachRegionRelatedToCoordinates(int row, int col, Consumer<SudokuCell> action) {
        this.forEachInRow(row, action);
        this.forEachInColumn(col, action);
        this.forEachInSubgrid(row, col, action);
    }

    private void forEachInRow(int row, Consumer<SudokuCell> action) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            action.accept(getElement(row, col));
        }
    }

    private void forEachInColumn(int col, Consumer<SudokuCell> action) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            action.accept(getElement(row, col));
        }
    }

    private void forEachInSubgrid(int row, int col, Consumer<SudokuCell> action) {
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;

        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                action.accept(getElement(startRow + r, startCol + c));
            }
        }
    }
}
