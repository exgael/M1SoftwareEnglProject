package org.sudokusolver.Core.Regions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sudokusolver.Core.SudokuCell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegionTest {

    private Region region;
    private SudokuCell cell1;
    private SudokuCell cell2;
    private SudokuCell cell3;

    @BeforeEach
    void setUp() {
        cell1 = mock(SudokuCell.class);
        cell2 = mock(SudokuCell.class);
        cell3 = mock(SudokuCell.class);

        region = new Region(Arrays.asList(cell1, cell2, cell3));
    }

    @Test
    @DisplayName("getCells should return the list of cells in the region")
    void testGetCells() {
        List<SudokuCell> cells = region.getCells();
        assertEquals(3, cells.size(), "Region should have 3 cells");
        assertEquals(cell1, cells.get(0), "First cell should be cell1");
        assertEquals(cell2, cells.get(1), "Second cell should be cell2");
        assertEquals(cell3, cells.get(2), "Third cell should be cell3");
    }

    @Test
    @DisplayName("containsValue should return true if the value is in any cell")
    void testContainsValue() {
        when(cell1.getNumber()).thenReturn(5);
        when(cell2.getNumber()).thenReturn(0);
        when(cell3.getNumber()).thenReturn(7);

        assertTrue(region.containsValue(5), "Region should contain the value 5");
        assertFalse(region.containsValue(3), "Region should not contain the value 3");
    }

    @Test
    @DisplayName("isSolved should return true if all cells are solved")
    void testIsSolved() {
        when(cell1.isSolved()).thenReturn(true);
        when(cell2.isSolved()).thenReturn(true);
        when(cell3.isSolved()).thenReturn(false);

        assertFalse(region.isSolved(), "Region should not be solved if any cell is unsolved");

        when(cell3.isSolved()).thenReturn(true);
        assertTrue(region.isSolved(), "Region should be solved if all cells are solved");
    }

    @Test
    @DisplayName("removeCandidate should remove the candidate from all cells")
    void testRemoveCandidate() {
        region.removeCandidate(5);

        verify(cell1).removeCandidate(5);
        verify(cell2).removeCandidate(5);
        verify(cell3).removeCandidate(5);
    }

    @Test
    @DisplayName("findCellsWithCandidateCount should return cells with the specified candidate count")
    void testFindCellsWithCandidateCount() {
        when(cell1.candidateCount()).thenReturn(2);
        when(cell2.candidateCount()).thenReturn(3);
        when(cell3.candidateCount()).thenReturn(2);

        List<SudokuCell> result = region.findCellsWithCandidateCount(2);
        assertEquals(2, result.size(), "Two cells should have candidate count of 2");
        assertTrue(result.contains(cell1), "Result should contain cell1");
        assertTrue(result.contains(cell3), "Result should contain cell3");
    }

    @Test
    @DisplayName("findUnsolvedCells should return unsolved cells")
    void testFindUnsolvedCells() {
        when(cell1.isSolved()).thenReturn(false);
        when(cell2.isSolved()).thenReturn(true);
        when(cell3.isSolved()).thenReturn(false);

        List<SudokuCell> unsolvedCells = region.findUnsolvedCells();
        assertEquals(2, unsolvedCells.size(), "Two cells should be unsolved");
        assertTrue(unsolvedCells.contains(cell1), "Unsolved cells should contain cell1");
        assertTrue(unsolvedCells.contains(cell3), "Unsolved cells should contain cell3");
    }

    @Test
    @DisplayName("removeCandidate with excludeCells should remove the candidate from all cells except excluded ones")
    void testRemoveCandidateWithExclusion() {
        Set<SudokuCell> excludeCells = new HashSet<>(Set.of(cell1));

        region.removeCandidate(5, excludeCells);

        verify(cell1, never()).removeCandidate(5);
        verify(cell2).removeCandidate(5);
        verify(cell3).removeCandidate(5);
    }

    @Test
    @DisplayName("removeCandidates should remove all candidates from cells except excluded ones")
    void testRemoveCandidatesWithExclusion() {
        Set<Integer> candidates = new HashSet<>(Set.of(1, 2, 3));
        Set<SudokuCell> excludeCells = new HashSet<>(Set.of(cell2));

        region.removeCandidates(candidates, excludeCells);

        verify(cell1).removeCandidate(1);
        verify(cell1).removeCandidate(2);
        verify(cell1).removeCandidate(3);

        verify(cell2, never()).removeCandidate(anyInt());

        verify(cell3).removeCandidate(1);
        verify(cell3).removeCandidate(2);
        verify(cell3).removeCandidate(3);
    }

    @Test
    @DisplayName("forEach should apply an action to each cell in the region")
    void testForEach() {
        Consumer<SudokuCell> action = mock(Consumer.class);

        region.forEach(action);

        verify(action).accept(cell1);
        verify(action).accept(cell2);
        verify(action).accept(cell3);
    }
}
