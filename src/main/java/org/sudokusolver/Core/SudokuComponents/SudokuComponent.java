package org.sudokusolver.Core.SudokuComponents;

public interface SudokuComponent {
    boolean isSolved();
    void removeCandidate(int candidate);
}
