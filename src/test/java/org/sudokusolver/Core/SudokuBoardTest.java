//package org.sudokusolver.Core;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.sudokusolver.Solver.Regions.Region;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class SudokuBoardTest {
//
//    private SudokuBoard board;
//
//    int[] easySudoku = new int[]{
//            2, 9, 0,  0, 7, 1,  0, 0, 0,
//            0, 8, 0,  3, 0, 9,  0, 0, 6,
//            0, 4, 0,  0, 0, 0,  0, 0, 0,
//            9, 0, 7,  0, 8, 0,  2, 0, 4,
//            0, 0, 0,  9, 0, 0,  6, 0, 0,
//            0, 0, 8,  0, 2, 0,  9, 1, 3,
//            0, 2, 9,  7, 0, 4,  0, 3, 8,
//            8, 0, 5,  1, 0, 0,  0, 7, 9,
//            0, 7, 4,  0, 9, 0,  1, 6, 2
//    };
//
//    int[] easySudokuSolution = new int[]{
//            2, 9, 6,  8, 7, 1,  3, 4, 5,
//            5, 8, 1,  3, 4, 9,  7, 2, 6,
//            7, 4, 3,  2, 5, 6,  8, 9, 1,
//            9, 1, 7,  6, 8, 3,  2, 5, 4,
//            4, 3, 2,  9, 1, 5,  6, 8, 7,
//            6, 5, 8,  4, 2, 7,  9, 1, 3,
//            1, 2, 9,  7, 6, 4,  5, 3, 8,
//            8, 6, 5,  1, 3, 2,  4, 7, 9,
//            3, 7, 4,  5, 9, 8,  1, 6, 2
//    };
//
//    @BeforeEach
//    void setUp() {
//        board = new SudokuBoard(easySudoku);
//    }
//
//    @Test
//    @DisplayName("getBoardSize should return the correct board size")
//    void testGetBoardSize() {
//        assertEquals(9, board.getBoardSize(), "Board size should be 9");
//    }
//
//    @Test
//    @DisplayName("SudokuBoard should be initialized with values from the input array")
//    void testBoardInitialization() {
//        for (int row = 0; row < board.getBoardSize(); row++) {
//            for (int col = 0; col < board.getBoardSize(); col++) {
//                int index = row * board.getBoardSize() + col;
//                int expectedValue = easySudoku[index];
//                assertEquals(expectedValue, board.getValue(row, col),
//                        String.format("The value at (%d,%d) should be %d", row, col, expectedValue));
//            }
//        }
//    }
//
//    @Test
//    @DisplayName("initializeRegions should correctly initialize regions with proper cells")
//    void testInitializeRegions() {
//        // Test that rows are correctly initialized
//        for (int row = 0; row < board.getBoardSize(); row++) {
//            Region rowRegion = board.getRowRegion(row);
//            List<SudokuCell> rowCells = rowRegion.getCells();
//            assertEquals(board.getBoardSize(), rowCells.size(), "Row region should have 9 cells");
//            for (int col = 0; col < board.getBoardSize(); col++) {
//                assertEquals(board.getElement(row, col), rowCells.get(col), String.format("Row region cell at index %d should be cell (%d,%d)", col, row, col));
//            }
//        }
//
//        // Test that columns are correctly initialized
//        for (int col = 0; col < board.getBoardSize(); col++) {
//            Region colRegion = board.getColumnRegion(col);
//            List<SudokuCell> colCells = colRegion.getCells();
//            assertEquals(board.getBoardSize(), colCells.size(), "Column region should have 9 cells");
//            for (int row = 0; row < board.getBoardSize(); row++) {
//                assertEquals(board.getElement(row, col), colCells.get(row), String.format("Column region cell at index %d should be cell (%d,%d)", row, row, col));
//            }
//        }
//
//        // Test that subgrids are correctly initialized
//        int subgridIndex = 0;
//        for (int gridRow = 0; gridRow < 3; gridRow++) {
//            for (int gridCol = 0; gridCol < 3; gridCol++) {
//                Region subgridRegion = board.getSubgridRegion(subgridIndex);
//                List<SudokuCell> subgridCells = subgridRegion.getCells();
//                assertEquals(board.getBoardSize(), subgridCells.size(), "Subgrid region should have 9 cells");
//                int cellIndex = 0;
//                for (int row = gridRow * 3; row < gridRow * 3 + 3; row++) {
//                    for (int col = gridCol * 3; col < gridCol * 3 + 3; col++) {
//                        assertEquals(board.getElement(row, col), subgridCells.get(cellIndex), String.format("Subgrid cell at index %d should be cell (%d,%d)", cellIndex, row, col));
//                        cellIndex++;
//                    }
//                }
//                subgridIndex++;
//            }
//        }
//    }
//
//
//    @Test
//    @DisplayName("Region getters should return correct data after board initialization")
//    void testRegionsAfterInitialization() {
//        // Check first row region
//        Region rowRegion = board.getRowRegion(0);
//        int[] expectedRowValues = {2, 9, 0, 0, 7, 1, 0, 0, 0};
//        for (int col = 0; col < 9; col++) {
//            assertEquals(expectedRowValues[col], rowRegion.getCells().get(col).getNumber(),
//                    "Row region should return correct values");
//        }
//
//        // Check first column region
//        Region colRegion = board.getColumnRegion(0);
//        int[] expectedColValues = {2, 0, 0, 9, 0, 0, 0, 8, 0};
//        for (int row = 0; row < 9; row++) {
//            assertEquals(expectedColValues[row], colRegion.getCells().get(row).getNumber(),
//                    "Column region should return correct values");
//        }
//
//        // Check subgrid region (0,0)
//        Region subgridRegion = board.getSubgridRegion(0, 0);
//        int[] expectedSubgridValues = {2, 9, 0, 0, 8, 0, 0, 4, 0};
//        for (int i = 0; i < 9; i++) {
//            assertEquals(expectedSubgridValues[i], subgridRegion.getCells().get(i).getNumber(),
//                    "Subgrid region should return correct values");
//        }
//    }
//
//    @Test
//    @DisplayName("setValue should correctly set the value in the specified cell")
//    void testSetValue() {
//        board.setValue(0, 0, 5);
//        assertEquals(5, board.getValue(0, 0), "The value should be set to 5 in cell (0,0)");
//
//        board.setValue(8, 8, 9);
//        assertEquals(9, board.getValue(8, 8), "The value should be set to 9 in cell (8,8)");
//    }
//
//    @Test
//    @DisplayName("setValue should validate and reject invalid values")
//    void testSetValueInvalid() {
//        assertThrows(IllegalArgumentException.class, () -> board.setValue(0, 0, 10), "Setting value above 9 should throw IllegalArgumentException");
//        assertThrows(IllegalArgumentException.class, () -> board.setValue(0, 0, -1), "Setting negative value should throw IllegalArgumentException");
//    }
//
//    @Test
//    @DisplayName("isSolved should return false if any region is not solved")
//    void testIsSolvedFalse() {
//        assertFalse(board.isSolved(), "The board should not be solved initially");
//    }
//
//    @Test
//    @DisplayName("isSolved should return true if all regions are solved")
//    void testIsSolvedTrue() {
//        SudokuBoard solvedBoard = new SudokuBoard(easySudokuSolution);
//        assertTrue(solvedBoard.isSolved(), "The board should be solved");
//    }
//
//    @Test
//    @DisplayName("getPossibleValues should return correct possible values for a cell")
//    void testGetPossibleValues() {
//        List<Integer> possibleValues = board.getPossibleValues(0, 2);
//        assertTrue(possibleValues.containsAll(List.of(3, 6)),
//                "Possible values should be [3,6]");
//        possibleValues = board.getPossibleValues(1, 0);
//        assertTrue(possibleValues.containsAll(List.of(1, 5, 7)),
//                "Possible values should be [1,5,7]");
//        possibleValues = board.getPossibleValues(2, 5);
//        assertTrue(possibleValues.containsAll(List.of(2, 5, 6, 8)),
//                "Possible values should be [2,5,6,8]");
//    }
//
//    @Test
//    @DisplayName("getPossibleValues should return empty list if cell is already solved")
//    void testGetPossibleValuesSolvedCell() {
//        board.setValue(4, 4, 5);
//        List<Integer> possibleValues = board.getPossibleValues(4, 4);
//        assertTrue(possibleValues.isEmpty(), "No possible values should be available for a solved cell");
//    }
//
//    @Test
//    @DisplayName("getPossibleValues should return correct possible values for unsolved cells")
//    void testGetPossibleValuesForUnsolvedCells() {
//        // (0, 2) is unsolved in the easySudoku
//        List<Integer> possibleValues = board.getPossibleValues(0, 2);
//        assertEquals(List.of(3, 6), possibleValues, "Possible values for (0,2) should be 3, 6");
//
//        // (4, 4) is unsolved in the easySudoku
//        possibleValues = board.getPossibleValues(4, 4);
//        assertEquals(List.of(1, 3, 4, 5), possibleValues, "Possible values for (4,4) should be 1, 3, 4, 5, 7");
//    }
//
//    @Test
//    @DisplayName("Complete subgrid should have no possible values")
//    void testCompleteSubgrid() {
//        board.setValue(0, 0, 1);
//        board.setValue(0, 1, 2);
//        board.setValue(0, 2, 3);
//        board.setValue(1, 0, 4);
//        board.setValue(1, 1, 5);
//        board.setValue(1, 2, 6);
//        board.setValue(2, 0, 7);
//        board.setValue(2, 1, 8);
//        board.setValue(2, 2, 9);
//
//        List<Integer> possibleValues = board.getPossibleValues(0, 0);
//        assertTrue(possibleValues.isEmpty(), "No possible values should be available for a complete subgrid");
//    }
//
//    @Test
//    @DisplayName("setValue should trigger remove candidates from related regions")
//    void testSetValueRemovesCandidatesFromRegions() {
//        Region rowRegion = mock(Region.class);
//        Region colRegion = mock(Region.class);
//        Region subgridRegion = mock(Region.class);
//
//        SudokuBoard spyBoard = spy(board);
//
//        doReturn(rowRegion).when(spyBoard).getRowRegion(anyInt());
//        doReturn(colRegion).when(spyBoard).getColumnRegion(anyInt());
//        doReturn(subgridRegion).when(spyBoard).getSubgridRegion(anyInt(), anyInt());
//
//        spyBoard.setValue(1, 1, 8);
//
//        verify(rowRegion).removeCandidate(8);
//        verify(colRegion).removeCandidate(8);
//        verify(subgridRegion).removeCandidate(8);
//    }
//
//    @Test
//    @DisplayName("setValue should remove the value from all related regions")
//    void testSetValueRemovesValueFromRegions() {
//        SudokuCell cell = board.getElement(0, 8);
//        board.setValue(0, 0, 5);
//        var rowRegion = board.getRowRegion(0);
//        var columnRegion = board.getColumnRegion(0);
//        var subgridRegion = board.getSubgridRegion(0);
//
//        assertFalse(rowRegion.getCells().stream().anyMatch(c -> c.hasCandidate(5)), "The value 5 should be removed from the all candidates in the row region");
//        assertFalse(columnRegion.getCells().stream().anyMatch(c -> c.hasCandidate(5)), "The value 5 should be removed from the all candidates in the column region");
//        assertFalse(subgridRegion.getCells().stream().anyMatch(c -> c.hasCandidate(5)), "The value 5 should be removed from the all candidates in the subgrid region");
//    }
//
//    @Test
//    @DisplayName("setValue should correctly set the value and update candidates in related regions")
//    void testSetValueAndUpdateRegions() {
//        board.setValue(0, 2, 6); // Set value in an unsolved cell
//
//        assertEquals(6, board.getValue(0, 2), "The value should be set to 6 in cell (0,2)");
//
//        // Ensure value is removed as a candidate in the row, column, and subgrid
//        Region rowRegion = board.getRowRegion(0);
//        Region colRegion = board.getColumnRegion(2);
//        Region subgridRegion = board.getSubgridRegion(0, 0);
//
//        assertFalse(rowRegion.getCells().stream().anyMatch(c -> c.hasCandidate(6)), "Candidate 6 should be removed from row region");
//        assertFalse(colRegion.getCells().stream().anyMatch(c -> c.hasCandidate(6)), "Candidate 6 should be removed from column region");
//        assertFalse(subgridRegion.getCells().stream().anyMatch(c -> c.hasCandidate(6)), "Candidate 6 should be removed from subgrid region");
//    }
//
//    @Test
//    @DisplayName("setValue should remove all candidates from the cell")
//    void testSetValueRemovesCandidateFromCell() {
//        SudokuCell cell = board.getElement(0, 8);
//        board.setValue(0, 0, 5);
//        assertTrue(cell.getCandidates().isEmpty(), "All candidates should be removed from the cell");
//    }
//
//    @Test
//    @DisplayName("canPlaceValue should return false if value exists in row, column, or subgrid")
//    void testCanPlaceValueFalse() {
//        board.setValue(0, 0, 5);
//        board.setValue(0, 1, 6);
//        board.setValue(3, 0, 7);
//
//        assertFalse(board.canPlaceValue(0, 2, 5), "The value 5 should not be placeable in the same row");
//        assertFalse(board.canPlaceValue(1, 0, 6), "The value 6 should not be placeable in the same column");
//        assertFalse(board.canPlaceValue(3, 1, 7), "The value 7 should not be placeable in the same subgrid");
//    }
//
//    @Test
//    @DisplayName("canPlaceValue should return true if value can be placed in cell")
//    void testCanPlaceValueTrue() {
//        assertTrue(board.canPlaceValue(0, 8, 5), "The value 5 should be placeable in the cell (0,8)");
//        assertTrue(board.canPlaceValue(1, 2, 1), "The value 1 should be placeable in the cell (1,2)");
//        assertTrue(board.canPlaceValue(4, 7, 5), "The value 5 should be placeable in the cell (4,7)");
//    }
//
//    @Test
//    @DisplayName("forEachRegion should apply the provided action to all regions")
//    void testForEachRegion() {
//        Consumer<Region> action = mock(Consumer.class);
//        board.forEachRegion(action);
//        verify(action, times(27)).accept(any(Region.class));
//    }
//
//    @Test
//    @DisplayName("isSolved should return true if all cells are correctly filled")
//    void testIsSolved() {
//        // Fill the board with a solution (mock solution for test purposes)
//        for (int row = 0; row < 9; row++) {
//            for (int col = 0; col < 9; col++) {
//                if (board.getValue(row, col) == 0) {
//                    board.setValue(row, col, (row * 9 + col) % 9 + 1); // Mock filling with arbitrary values
//                }
//            }
//        }
//
//        assertTrue(board.isSolved(), "The board should be solved after filling all values");
//    }
//
//    @Test
//    @DisplayName("debugDescription should return a non-null string representation of the board")
//    void testDebugDescription() {
//        String description = board.debugDescription();
//        assertNotNull(description, "debugDescription should return a non-null string");
//        assertFalse(description.isEmpty(), "debugDescription should not be empty");
//    }
//}
