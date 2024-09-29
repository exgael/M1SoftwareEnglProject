package org.sudokusolver.Strategy.Sudoku;

import org.sudokusolver.Utils.Inspectable;
import org.sudokusolver.Utils.Observer;
import org.sudokusolver.Utils.Subject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuCell implements Subject<SudokuCellUpdate>, Inspectable {
    private final Set<Integer> candidates;
    private final int row, col;
    private final List<Observer<SudokuCellUpdate>> observers = new ArrayList<>();
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
            this.notifyObservers();
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
            candidates.clear();

            if (value != 0) {
                try {
                    Thread.sleep(100); // Sleep for 0.1 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    System.err.println("Sleep interrupted: " + e.getMessage());
                }
                System.out.println("Cell [" + row + ", " + col + "] set to " + value);
                this.notifyObservers();
            }
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

    public void addCandidate(int candidate) {
        if (candidates.add(candidate)) {
      //      this.notifyObservers();
        }
    }

    public boolean removeCandidate(int candidate) {
        //     this.notifyObservers();
        return candidates.remove(candidate);
    }

    public boolean removeCandidates(Set<Integer> candidatesToRemove) {
        //     this.notifyObservers();
        return candidates.removeAll(candidatesToRemove);
    }

    public void clearCandidates() {
        candidates.clear();
     //   this.notifyObservers(new SudokuCellUpdate(row, col, value, candidates));
    }

    public boolean hasCandidate(int candidate) {
        return candidates.contains(candidate);
    }

    public int candidateCount() {
        return candidates.size();
    }

    @Override
    public void addObserver(Observer<SudokuCellUpdate> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<SudokuCellUpdate> observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        notifyObservers(new SudokuCellUpdate(row, col, value, candidates));
    }

    @Override
    public void notifyObservers(SudokuCellUpdate data) {
        for (Observer<SudokuCellUpdate> observer : observers) {
            observer.update(data);
        }
    }

    @Override
    public String debugDescription() {
        StringBuilder sb = new StringBuilder();
        if (value != 0) {
            // Centered number in the cell
            sb.append(String.format("  %1d  ", value));
        } else {
            sb.append(String.format("%1d    ", this.candidates.size()));
        }

        return sb.toString();
    }
}
