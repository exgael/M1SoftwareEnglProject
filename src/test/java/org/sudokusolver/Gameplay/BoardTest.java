package org.sudokusolver.Gameplay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.sudokusolver.Gameplay.Sudoku.Board;
import org.sudokusolver.Utils.Inspectable;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    static class MockCell implements Inspectable {
        private int value;

        public MockCell(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String debugDescription() {
            return Integer.toString(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            BoardTest.MockCell other = (BoardTest.MockCell) obj;
            return value == other.getValue();
        }
    }

    private Board<MockCell> board;
    private final int ROWS = 3;
    private final int COLS = 4;

    @BeforeEach
    void setUp() {
        board = new Board<>(ROWS, COLS, () -> new MockCell(0));
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
                assertEquals(new MockCell(0), board.getElement(row, col));
            }
        }
    }

    @Test
    @DisplayName("setValue should update cell value")
    void testSetValue() {
        board.setElement(1, 2, new MockCell(5));
        assertEquals(new MockCell(5), board.getElement(1, 2));
    }

    @Test
    @DisplayName("getValue should return correct value")
    void testGetValue() {
        board.setElement(0, 1, new MockCell(3));
        assertEquals(new MockCell(3), board.getElement(0, 1));
    }

    @Test
    @DisplayName("Board should throw exception for invalid coordinates")
    void testInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> board.getElement(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> board.getElement(0, COLS));
        assertThrows(IllegalArgumentException.class, () -> board.setElement(ROWS, 0, new MockCell(1)));
    }

    @Test
    @DisplayName("Board creation should throw exception for invalid dimensions")
    void testInvalidBoardCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Board<>(0, 5, () -> new MockCell(0)));
        assertThrows(IllegalArgumentException.class, () -> new Board<>(5, -1, () -> new MockCell(0)));
    }
}