package org.sudokusolver.Core.Regions;

import org.sudokusolver.Core.SudokuCell;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Region {

    List<SudokuCell> cells;

    public Region(List<SudokuCell> cells) {
        this.cells = cells;
    }

    public List<SudokuCell> getCells() {
        return this.cells;
    }

    public boolean containsValue(int value) {
        return this.cells
                .stream()
                .anyMatch(sudokuCell -> sudokuCell.getNumber() == value);
    }

    public boolean isSolved() {
        return this.cells.stream().allMatch(SudokuCell::isSolved);
    }

    public void removeCandidate(int candidate) {
        this.forEach(sudokuCell -> sudokuCell.removeCandidate(candidate));
    }

    public List<SudokuCell> findCellsWithCandidateCount(int candidateCount) {
        return this.cells.stream()
                .filter(sudokuCell -> sudokuCell.candidateCount() == candidateCount)
                .toList();
    }

    public List<SudokuCell> findUnsolvedCells() {
        return this.cells.stream()
                .filter(sudokuCell -> !sudokuCell.isSolved())
                .toList();
    }

    public void removeCandidate(int candidate, Set<SudokuCell> excludeCells) {
        this.cells.stream()
                .filter(sudokuCell -> !excludeCells.contains(sudokuCell))
                .forEach(sudokuCell -> sudokuCell.removeCandidate(candidate));
    }

    public void removeCandidates(Set<Integer> candidates, Set<SudokuCell> excludeCells) {
        this.cells.stream()
                .filter(sudokuCell -> !excludeCells.contains(sudokuCell))
                .forEach(sudokuCell -> candidates.forEach(sudokuCell::removeCandidate));
    }

    public void forEach(Consumer<SudokuCell> action) {
        this.cells.forEach(action);
    }
}
