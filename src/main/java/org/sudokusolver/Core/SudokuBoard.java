package org.sudokusolver.Core;

import java.util.ArrayList;
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
        forEachInRow(row, cell -> cell.removeCandidate(value));
        forEachInColumn(col, cell -> cell.removeCandidate(value));
        forEachInSubgrid(row, col, cell -> cell.removeCandidate(value));
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
        List<SudokuCell> cells = new ArrayList<>();
        switch (regionType) {
            case ROW -> cells = this.findsCellWithCandidateCountInRow(index, count);
            case COLUMN -> cells = this.findCellsWithCandidateCountInColumn(index, count);
            case SUBGRID -> {
                int[] gridCoordinates = getSubgridStartCoordinates(index);
                cells = this.findCellsWithCandidateCountInSubgrid(gridCoordinates[0], gridCoordinates[1], count);
            }
        }
        return cells;
    }

    private List<SudokuCell> findsCellWithCandidateCountInRow(int row, int count) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInRow(row, cell -> {
            if (cell.candidateCount() == count) {
                cells.add(cell);
            }
        });
        return cells;
    }

    private List<SudokuCell> findCellsWithCandidateCountInColumn(int col, int count) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInColumn(col, cell -> {
            if (cell.candidateCount() == count) {
                cells.add(cell);
            }
        });
        return cells;
    }

    private List<SudokuCell> findCellsWithCandidateCountInSubgrid(int row, int col, int count) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInSubgrid(row, col, cell -> {
            if (cell.candidateCount() == count) {
                cells.add(cell);
            }
        });
        return cells;
    }

    public List<SudokuCell> findUnsolvedCellsInRegion(int index, RegionType regionType) {
        List<SudokuCell> cells = new ArrayList<>();
        switch (regionType) {
            case ROW -> cells = this.findUnsolvedCellsInRow(index);
            case COLUMN -> cells = this.findUnsolvedCellsInColumns(index);
            case SUBGRID -> {
                int[] gridCoordinates = getSubgridStartCoordinates(index);
                cells = this.findUnsolvedCellsInSubgrid(gridCoordinates[0], gridCoordinates[1]);
            }
        }
        return cells;
    }

    private List<SudokuCell> findUnsolvedCellsInSubgrid(int row, int col) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInSubgrid(row, col, cell -> {
            if (!cell.isSolved()) {
                cells.add(cell);
            }
        });
        return cells;
    }

    private List<SudokuCell> findUnsolvedCellsInRow(int row) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInRow(row, cell -> {
            if (!cell.isSolved()) {
                cells.add(cell);
            }
        });
        return cells;
    }

    private List<SudokuCell> findUnsolvedCellsInColumns(int col) {
        List<SudokuCell> cells = new ArrayList<>();
        this.forEachInColumn(col, cell -> {
            if (!cell.isSolved()) {
                cells.add(cell);
            }
        });
        return cells;
    }

    private int[] getSubgridStartCoordinates(int index) {
        int gridRow = Math.floorDiv(index, SUBGRID_SIZE) * SUBGRID_SIZE;
        int gridCol = (index % SUBGRID_SIZE) * SUBGRID_SIZE;
        return new int[]{gridRow, gridCol};
    }

    private boolean isValueInRow(int row, int value) {
        final boolean[] found = {false};
        forEachInRow(row, cell -> {
            if (cell.getNumber() == value) {
                found[0] = true;
            }
        });
        return found[0];
    }

    private boolean isValueInColumn(int col, int value) {
        final boolean[] found = {false};
        forEachInColumn(col, cell -> {
            if (cell.getNumber() == value) {
                found[0] = true;
            }
        });
        return found[0];
    }

    private boolean isValueInSubgrid(int row, int col, int value) {
        final boolean[] found = {false};
        forEachInSubgrid(row, col, cell -> {
            if (cell.getNumber() == value) {
                found[0] = true;
            }
        });
        return found[0];
    }

    private void forEachInRow(int index, Consumer<SudokuCell> action) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            action.accept(getElement(index, col));
        }
    }

    private void forEachInColumn(int index, Consumer<SudokuCell> action) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            action.accept(getElement(row, index));
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

    private boolean canPlaceValue(int row, int col, int value) {
        return getValue(row, col) == 0 &&
                !isValueInRow(row, value) &&
                !isValueInColumn(col, value) &&
                !isValueInSubgrid(row, col, value);
    }

    private void validateValue(int value) {
        if (value == 0) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
