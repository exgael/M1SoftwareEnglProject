package org.sudokusolver.Strategy.Sudoku;

import org.sudokusolver.Gameplay.Sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuBoard extends ObservableBoard<SudokuCell, SudokuUpdate> implements Sudoku {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int DEFAULT_VALUE = 0;


    public SudokuBoard() {
        super(BOARD_SIZE, BOARD_SIZE);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setElement(i, j, new SudokuCell(0, i, j));
            }
        }
    }

    @Override
    public void load(int[] grid) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int value = grid[i * BOARD_SIZE + j];
                setValue(i, j, value);
            }
        }
        initializeCandidates();

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



    public void setValue(int row, int col, int value) {
        validateValue(value);
        if (getElement(row, col).getValue() != DEFAULT_VALUE) {
            throw new IllegalArgumentException("Cell is already solved");
        }

        // Remove value from candidates in related rows, columns, and subgrids
        removeCandidateFromRow(row, value);
        removeCandidateFromColumn(col, value);
        removeCandidateFromSubgrid(row, col, value);

        getElement(row, col).setValue(value);
        notifyChangeAt(row, col);
    }

    private void notifyChangeAt(int row, int col) {
        var cell = getElement(row, col);
        var value = cell.getValue();
        var candidates = cell.getCandidates();
        SudokuUpdate update = new SudokuUpdate(row, col, value, candidates);
        super.notifyObservers(update);
    }

    public int[] getCandidates(int row, int col) {
        return getElement(row, col).getCandidates().stream().mapToInt(i -> i).toArray();
    }

    public void addCandidates(int[] candidates, int row, int col) {
        for (int candidate : candidates) {
            getElement(row, col).addCandidate(candidate);
        }
    }

    public void removeCandidates(int[] candidates, int row, int col) {
        for (int candidate : candidates) {
            getElement(row, col).removeCandidate(candidate);
        }
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

    private void initializeCandidates() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SudokuCell cell = getElement(row, col);
                if (cell.getValue() == DEFAULT_VALUE) {
                    List<Integer> candidates = getPossibleValues(row, col);
                    cell.initializeCandidates(candidates);
                }
            }
        }

        // assert each non default cell has at least one candidate
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SudokuCell cell = getElement(row, col);
                if (cell.getValue() == DEFAULT_VALUE) {
                    if (cell.getCandidates().isEmpty()) {
                        throw new IllegalStateException("No candidates for cell at row " + (row + 1) + " and column " + (col + 1));
                    }
                }
            }
        }
    }

    private List<Integer> getPossibleValues(int row, int col) {
        Set<Integer> possibleValues = IntStream.rangeClosed(1, BOARD_SIZE).boxed().collect(Collectors.toSet());
        removeValuesFromSet(possibleValues, getRowValues(row));
        removeValuesFromSet(possibleValues, getColumnValues(col));
        removeValuesFromSet(possibleValues, getSubgridValues(row, col));
        return new ArrayList<>(possibleValues);
    }

    private void removeValuesFromSet(Set<Integer> set, List<Integer> values) {
        values.forEach(set::remove);
    }

    private List<Integer> getRowValues(int row) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(col -> getElement(row, col).getValue())
                .filter(value -> value != DEFAULT_VALUE)
                .collect(Collectors.toList());
    }

    private List<Integer> getColumnValues(int col) {
        return IntStream.range(0, BOARD_SIZE)
                .mapToObj(row -> getElement(row, col).getValue())
                .filter(value -> value != DEFAULT_VALUE)
                .collect(Collectors.toList());
    }

    private List<Integer> getSubgridValues(int row, int col) {
        List<Integer> subgridValues = new ArrayList<>();
        forEachInSubgrid(row, col, (r, c) -> {
            int value = getElement(r, c).getValue();
            if (value != DEFAULT_VALUE) {
                subgridValues.add(value);
            }
        });
        return subgridValues;
    }

    private void removeCandidateFromRow(int row, int value) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            getElement(row, col).removeCandidate(value);
        }
    }

    private void removeCandidateFromColumn(int col, int value) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            getElement(row, col).removeCandidate(value);
        }
    }

    private void removeCandidateFromSubgrid(int row, int col, int value) {
        forEachInSubgrid(row, col, (r, c) -> getElement(r, c).removeCandidate(value));
    }

    private void forEachInSubgrid(int row, int col, BiConsumer<Integer, Integer> action) {
        int startRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        for (int rowOffset = 0; rowOffset < SUBGRID_SIZE; rowOffset++) {
            for (int colOffset = 0; colOffset < SUBGRID_SIZE; colOffset++) {
                action.accept(startRow + rowOffset, startCol + colOffset);
            }
        }
    }

    private void validateValue(int value) {
        if (value == DEFAULT_VALUE) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}