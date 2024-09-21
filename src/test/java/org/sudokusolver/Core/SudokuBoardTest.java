package org.sudokusolver.Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sudokusolver.Core.Regions.Region;
import org.sudokusolver.Core.Regions.RegionType;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SudokuBoardTest {

    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard();
    }

    @Test
    @DisplayName("SudokuBoard should be initialized with default values")
    void testBoardInitialization() {
        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                assertEquals(0, board.getValue(row, col), "Cell should be initialized to 0 (unsolved)");
            }
        }
    }

    @Test
    @DisplayName("setValue should correctly set the value in the specified cell")
    void testSetValue() {
        board.setValue(0, 0, 5);
        assertEquals(5, board.getValue(0, 0), "The value should be set to 5 in cell (0,0)");

        board.setValue(8, 8, 9);
        assertEquals(9, board.getValue(8, 8), "The value should be set to 9 in cell (8,8)");
    }

    @Test
    @DisplayName("setValue should validate and reject invalid values")
    void testSetValueInvalid() {
        assertThrows(IllegalArgumentException.class, () -> board.setValue(0, 0, 10), "Setting value above 9 should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> board.setValue(0, 0, -1), "Setting negative value should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("isSolved should return false if any region is not solved")
    void testIsSolved() {
        assertFalse(board.isSolved(), "The board should not be solved initially");

        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                board.setValue(row, col, (row * board.getBoardSize() + col) % 9 + 1);
            }
        }

        assertTrue(board.isSolved(), "The board should be solved after setting all values");
    }

    @Test
    @DisplayName("getPossibleValues should return correct possible values for a cell")
    void testGetPossibleValues() {
        board.setValue(0, 0, 5);
        board.setValue(0, 1, 6);
        board.setValue(0, 2, 7);

        // Ensure row-wise filtering is correct
        List<Integer> possibleValues = board.getPossibleValues(0, 3);
        assertEquals(List.of(1, 2, 3, 4, 8, 9), possibleValues, "The possible values should exclude those in the same row");

        // Test subgrid exclusion
        board.setValue(3, 0, 2);
        board.setValue(3, 1, 9);
        possibleValues = board.getPossibleValues(3, 2);
        assertEquals(List.of(1, 3, 4, 5, 6, 8), possibleValues, "The possible values should exclude those in the same subgrid and row");
    }


    @Test
    @DisplayName("getPossibleValues should return empty list if cell is already solved")
    void testGetPossibleValuesSolvedCell() {
        board.setValue(4, 4, 5);
        List<Integer> possibleValues = board.getPossibleValues(4, 4);
        assertTrue(possibleValues.isEmpty(), "No possible values should be available for a solved cell");
    }

    @Test
    @DisplayName("setValue should remove candidates from related regions")
    void testSetValueRemovesCandidatesFromRegions() {
        Region rowRegion = mock(Region.class);
        Region colRegion = mock(Region.class);
        Region subgridRegion = mock(Region.class);

        SudokuBoard spyBoard = spy(board);

        doReturn(rowRegion).when(spyBoard).getRowRegion(anyInt());
        doReturn(colRegion).when(spyBoard).getColumnRegion(anyInt());
        doReturn(subgridRegion).when(spyBoard).getSubgridRegion(anyInt(), anyInt());

        spyBoard.setValue(1, 1, 8);

        verify(rowRegion).removeCandidate(8);
        verify(colRegion).removeCandidate(8);
        verify(subgridRegion).removeCandidate(8);
    }

    @Test
    @DisplayName("canPlaceValue should return false if value exists in row, column, or subgrid")
    void testCanPlaceValueFalse() {
        board.setValue(0, 0, 5);
        board.setValue(0, 1, 6);
        board.setValue(3, 0, 7);

        assertFalse(board.canPlaceValue(0, 2, 5), "The value 5 should not be placeable in the same row");
        assertFalse(board.canPlaceValue(1, 0, 6), "The value 6 should not be placeable in the same column");
        assertFalse(board.canPlaceValue(3, 1, 7), "The value 7 should not be placeable in the same subgrid");
    }

    @Test
    @DisplayName("canPlaceValue should return true if value can be placed in cell")
    void testCanPlaceValueTrue() {
        board.setValue(0, 0, 5);
        board.setValue(1, 1, 6);
        board.setValue(2, 2, 7);

        assertTrue(board.canPlaceValue(0, 2, 1), "The value 1 should be placeable in the cell (0,2)");
        assertTrue(board.canPlaceValue(3, 0, 9), "The value 9 should be placeable in the cell (3,0)");
    }

    @Test
    @DisplayName("forEachRegion should apply the provided action to all regions")
    void testForEachRegion() {
        Consumer<Region> action = mock(Consumer.class);
        board.forEachRegion(action);
        verify(action, times(27)).accept(any(Region.class));
    }
}
