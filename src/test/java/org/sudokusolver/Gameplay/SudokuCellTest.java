package org.sudokusolver.Gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuCellTest {

    private SudokuCell cell;

    @BeforeEach
    void setUp() {
        cell = new SudokuCell();
    }

    @Test
    @DisplayName("New cell should have no number and no candidates")
    void testInitialState() {
        assertEquals(0, cell.getNumber(), "Cell should be initialized with number 0");
        assertTrue(cell.getCandidates().isEmpty(), "Cell should have no candidates on initialization");
    }

    @Test
    @DisplayName("initializeCandidates should add candidates only if number is not set")
    void testInitializeCandidates() {
        cell.initializeCandidates(Arrays.asList(1, 2, 3, 4));
        Set<Integer> expectedCandidates = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        assertEquals(expectedCandidates, cell.getCandidates(), "Candidates should be added if the number is 0");

        // Now set a number and try to initialize candidates again
        cell.setNumber(5);
        cell.initializeCandidates(Arrays.asList(6, 7, 8));
        assertEquals(0, cell.getCandidates().size(), "Candidates should not be added if the number is set");
    }

    @Test
    @DisplayName("setNumber should clear candidates")
    void testSetNumberClearsCandidates() {
        cell.initializeCandidates(Arrays.asList(1, 2, 3, 4));
        cell.setNumber(9);
        assertEquals(9, cell.getNumber(), "Number should be set to the provided value");
        assertTrue(cell.getCandidates().isEmpty(), "Candidates should be cleared after setting a number");
    }

    @Test
    @DisplayName("removeCandidate should remove a candidate")
    void testRemoveCandidate() {
        cell.initializeCandidates(Arrays.asList(1, 2, 3));
        cell.removeCandidate(2);
        Set<Integer> expectedCandidates = new HashSet<>(Arrays.asList(1, 3));
        assertEquals(expectedCandidates, cell.getCandidates(), "Candidate 2 should be removed from the candidates list");
    }

    @Test
    @DisplayName("hasCandidate should return true if candidate exists")
    void testHasCandidate() {
        cell.initializeCandidates(Arrays.asList(1, 2, 3));
        assertTrue(cell.hasCandidate(1), "Cell should have candidate 1");
        assertFalse(cell.hasCandidate(4), "Cell should not have candidate 4");
    }

    @Test
    @DisplayName("isSolved should return true if the number is set")
    void testIsSolved() {
        assertFalse(cell.isSolved(), "Cell should not be solved initially");
        cell.setNumber(5);
        assertTrue(cell.isSolved(), "Cell should be solved after setting a number");
    }

    @Test
    @DisplayName("candidateCount should return the correct number of candidates")
    void testCandidateCount() {
        cell.initializeCandidates(Arrays.asList(1, 2, 3, 4));
        assertEquals(4, cell.candidateCount(), "Candidate count should be 4");
        cell.removeCandidate(2);
        assertEquals(3, cell.candidateCount(), "Candidate count should decrease to 3 after removing one");
    }
}
