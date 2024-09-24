package org.sudokusolver.Gameplay;

import org.sudokusolver.Utils.Inspectable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuCell implements Inspectable {
    private final Set<Integer> candidates;
    private int number;

    public SudokuCell() {
        this.number = 0; // 0 means no number set
        this.candidates = new HashSet<>();
    }

    public void initializeCandidates(List<Integer> candidates) {
        if (this.number == 0) {
            this.candidates.addAll(candidates);
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

    public void addCandidate(int candidate) {
        candidates.add(candidate);
    }

    public boolean removeCandidate(int candidate) {
        return candidates.remove(candidate);
    }

    public boolean removeCandidates(Set<Integer> candidates) {
        return this.candidates.removeAll(candidates);
    }

    public void clearCandidates() {
        candidates.clear();
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

    @Override
    public String toString() {
        return "" + number;
    }

    @Override
    public String debugDescription() {
        StringBuilder sb = new StringBuilder();
        if (number != 0) {
            // Centered number in the cell
            sb.append(String.format("  %1d  ", number));
        } else {
            sb.append(String.format("%1d    ", this.candidates.size()));
        }

        return sb.toString();
    }
}
