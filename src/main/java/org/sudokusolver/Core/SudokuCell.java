package org.sudokusolver.Core;

import java.util.HashSet;
import java.util.Set;

public class SudokuCell {
    private final Set<Integer> candidates;
    private int number;

    public SudokuCell() {
        this.number = 0; // 0 means no number set
        this.candidates = new HashSet<>();
    }

    public void initializeCandidates(int boardSize) {
        if (this.number == 0) {
            for (int i = 1; i <= boardSize; i++) {
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
