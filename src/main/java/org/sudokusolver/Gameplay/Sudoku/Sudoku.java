package org.sudokusolver.Gameplay.Sudoku;

import org.sudokusolver.Gameplay.Solvable;
import org.sudokusolver.Gameplay.Sudoku.Regions.Region;
import org.sudokusolver.Gameplay.Sudoku.Regions.RegionManager;

import java.util.List;
import java.util.stream.Stream;

public class Sudoku extends ObservableBoard<SudokuCell, SudokuUpdate> implements Modifiable, Solvable {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int DEFAULT_VALUE = 0;

    private final RegionManager regionManager;

    public Sudoku() {
        super(BOARD_SIZE, BOARD_SIZE);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setElement(i, j, new SudokuCell(0, i, j));
            }
        }
        regionManager = new RegionManager(this);
        initializeCandidates();
    }

    public void load(int[] grid) {

        // Reset
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                getElement(i, j).setValue(0);
                getElement(i, j).clearCandidates();
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int value = grid[i * BOARD_SIZE + j];
                setValueOnLoad(i, j, value);
            }
        }
        initializeCandidates();
    }

    private void setValueOnLoad(int row, int col, int value) {
        validateValue(value);
        getElement(row, col).setValue(value);
        notifyChangeAt(row, col);
    }

    private void initializeCandidates() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SudokuCell cell = getElement(row, col);
                if (cell.getValue() == DEFAULT_VALUE) {
                    List<Integer> candidates = regionManager.getPossibleValues(row, col);
                    cell.initializeCandidates(candidates);
                }
            }
        }
    }

    @Override
    public Stream<Region> streamRegions() {
        return regionManager.stream();
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getSubgridSize() {
        return SUBGRID_SIZE;
    }

    public int getValue(int row, int col) {
        return getElement(row, col).getValue();
    }

    public void setValue(int row, int col, int value) throws RuntimeException {
        validateValue(value);
        if (getElement(row, col).getValue() != DEFAULT_VALUE) {
            throw new RuntimeException("Cell is already solved");
        }
        regionManager.setValue(row, col, value);
        notifyChangeAt(row, col);
    }

    private void notifyChangeAt(int row, int col) {
        var cell = getElement(row, col);
        var value = cell.getValue();
        var candidates = cell.getCandidates();
        SudokuUpdate update = new SudokuUpdate(row, col, value, candidates);
        super.notifyObservers(update);
    }

    public boolean isSolved() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (getElement(row, col).getValue() == DEFAULT_VALUE) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validateValue(int value) {
        if (value == DEFAULT_VALUE) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}