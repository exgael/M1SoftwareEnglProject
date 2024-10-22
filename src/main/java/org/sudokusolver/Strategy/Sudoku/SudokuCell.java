package org.sudokusolver.Strategy.Sudoku;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuCell {
    private final Set<Integer> candidates;
    private final int row, col;
    private int value;

    public SudokuCell(int value, int row, int col) {
        this.candidates = new HashSet<>();
        this.value = value;
        this.row = row;
        this.col = col;
    }

    public void initializeCandidates(List<Integer> candidates) {
        if (value == 0) {
            this.candidates.addAll(candidates);
        }
    }

    public boolean isSolved() {
        return value != 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (this.value != value) {
            this.value = value;

            // Once a number is set, no candidates are needed
            clearCandidates();

            if (value != 0) {
                try {
                    Thread.sleep(10); // Sleep for 0.01 seconds
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void clearCandidates() {
        candidates.clear();
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

    public void addCandidate(int candidate) {
        candidates.add(candidate);
    }

    public void removeCandidate(int candidate) {
        candidates.remove(candidate);
    }

    public boolean removeCandidates(Set<Integer> candidatesToRemove) {
        return candidates.removeAll(candidatesToRemove);
    }

    public boolean hasCandidate(int candidate) {
        return candidates.contains(candidate);
    }

    public int candidateCount() {
        return candidates.size();
    }
}
