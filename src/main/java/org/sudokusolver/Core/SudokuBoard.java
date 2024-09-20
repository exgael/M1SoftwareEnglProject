package org.sudokusolver.Core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class SudokuBoard extends ObservableBoard<SudokuBoard.Cell> {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    public SudokuBoard() {
        super(9, 9, SudokuBoard.Cell::new);
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getSubgridSize() {
        return SUBGRID_SIZE;
    }

    public boolean isBoardSolved() {
        return false;
    }

    public void setValue(int row, int col, int value) {
        validateValue(value);
        getElement(row, col).setNumber(value);
    }

    public int getValue(int row, int col) {
        return getElement(row, col).getNumber();
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
                !isValueInRow(row, value) &&
                !isValueInColumn(col, value) &&
                !isValueInSubgrid(row, col, value);
    }

    public boolean isValueInRow(int row, int value) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (getValue(row, col) == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isValueInColumn(int col, int value) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (getValue(row, col) == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isValueInSubgrid(int row, int col, int value) {
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;

        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                if (getValue(startRow + r, startCol + c) == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearValue(int row, int col) {
        getElement(row, col).setNumber(0);
    }

    private void validateValue(int value) {
        if (value == 0) return;
        if (value < 1 || value > BOARD_SIZE) {
            String errorMessage = String.format("Invalid value: %d. Must be between 1 and %d", value, BOARD_SIZE);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static class Cell {
        private final Set<Integer> candidates;
        private int number;

        public Cell() {
            this.number = 0; // 0 means no number set
            this.candidates = new HashSet<>();
            initializeCandidates();
        }

        private void initializeCandidates() {
            if (this.number == 0) {
                for (int i = 1; i <= BOARD_SIZE; i++) {
                    candidates.add(i);
                }
            }
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
            candidates.clear(); // Once a number is set, no candidates are needed
        }

        public Set<Integer> getCandidates() {
            return candidates;
        }

        public void removeCandidate(int candidate) {
            candidates.remove(candidate);
        }

        public boolean hasCandidate(int candidate) {
            return candidates.contains(candidate);
        }

        public boolean isSolved() {
            return number != 0;
        }

        public int candidateCount() {
            return candidates.size();
        }
    }
}
