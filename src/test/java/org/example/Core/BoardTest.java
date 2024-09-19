package org.example.Core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board<Integer> board;
    private final int ROWS = 3;
    private final int COLS = 4;

    @BeforeEach
    void setUp() {
        board = new Board<>(ROWS, COLS, () -> 0);
    }

    @Test
    @DisplayName("Board should be created with correct dimensions")
    void testBoardCreation() {
        assertEquals(ROWS, board.getRows());
        assertEquals(COLS, board.getCols());
    }

    @Test
    @DisplayName("Board should be initialized with default values")
    void testBoardInitialization() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(0, board.getElement(row, col));
            }
        }
    }

    @Test
    @DisplayName("setValue should update cell value")
    void testSetValue() {
        board.setElement(1, 2, 5);
        assertEquals(5, board.getElement(1, 2));
    }

    @Test
    @DisplayName("getValue should return correct value")
    void testGetValue() {
        board.setElement(0, 1, 3);
        assertEquals(3, board.getElement(0, 1));
    }

    @Test
    @DisplayName("Board should throw exception for invalid coordinates")
    void testInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> board.getElement(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> board.getElement(0, COLS));
        assertThrows(IllegalArgumentException.class, () -> board.setElement(ROWS, 0, 1));
    }

    @Test
    @DisplayName("Board creation should throw exception for invalid dimensions")
    void testInvalidBoardCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Board<>(0, 5, () -> 0));
        assertThrows(IllegalArgumentException.class, () -> new Board<>(5, -1, () -> 0));
    }

    @Test
    @DisplayName("toString should return correct string representation")
    void testToString() {
        Board<String> stringBoard = new Board<>(2, 2, () -> "x");
        stringBoard.setElement(0, 0, "a");
        stringBoard.setElement(1, 1, "b");
        String expected = "a x \nx b \n";
        assertEquals(expected, stringBoard.toString());
    }

    @Test
    @DisplayName("Board should work with different types")
    void testGenericType() {
        Board<String> stringBoard = new Board<>(2, 2, () -> "empty");
        stringBoard.setElement(1, 1, "full");
        assertEquals("empty", stringBoard.getElement(0, 0));
        assertEquals("full", stringBoard.getElement(1, 1));
    }
}