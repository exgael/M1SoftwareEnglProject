package org.sudokusolver.Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sudokusolver.Core.Regions.Region;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardRegionTest {

    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(new int[81]);
    }

    @Test
    @DisplayName("getRowRegion should return the correct row data")
    void testGetRowRegion() {
        // Set values in the first row
        for (int col = 0; col < 9; col++) {
            board.setValue(0, col, col + 1); // Set row 0 to 1, 2, 3, ..., 9
        }

        Region rowRegion = board.getRowRegion(0);
        for (int col = 0; col < 9; col++) {
            assertEquals(col + 1, rowRegion.getCells().get(col).getNumber(),
                    "Row region should contain correct values");
        }
    }

    @Test
    @DisplayName("getColumnRegion should return the correct column data")
    void testGetColumnRegion() {
        // Set values in the first column
        for (int row = 0; row < 9; row++) {
            board.setValue(row, 0, row + 1); // Set column 0 to 1, 2, 3, ..., 9
        }

        Region columnRegion = board.getColumnRegion(0);
        for (int row = 0; row < 9; row++) {
            assertEquals(row + 1, columnRegion.getCells().get(row).getNumber(),
                    "Column region should contain correct values");
        }
    }

    @Test
    @DisplayName("getSubgridRegion should return the correct subgrid data")
    void testGetSubgridRegion() {
        // Set values in the top-left 3x3 subgrid
        int[][] values = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board.setValue(row, col, values[row][col]);
            }
        }

        Region subgridRegion = board.getSubgridRegion(0, 0);
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(values[row][col], subgridRegion.getCells().get(index).getNumber(),
                        "Subgrid region should contain correct values");
                index++;
            }
        }
    }

    @Test
    @DisplayName("All regions (rows, columns, and subgrids) should match the correct data")
    void testAllRegions() {
        // Set values in the board
        for (int i = 0; i < 9; i++) {
            board.setValue(0, i, i + 1); // Set first row values (1-9)
            board.setValue(i, 0, i + 1); // Set first column values (1-9)
        }

        // Check row region
        Region rowRegion = board.getRowRegion(0);
        for (int col = 0; col < 9; col++) {
            assertEquals(col + 1, rowRegion.getCells().get(col).getNumber(),
                    "Row region should match the set values");
        }

        // Check column region
        Region columnRegion = board.getColumnRegion(0);
        for (int row = 0; row < 9; row++) {
            assertEquals(row + 1, columnRegion.getCells().get(row).getNumber(),
                    "Column region should match the set values");
        }

        // Check subgrid region (top-left)
        int[][] subgridValues = {
                {1, 2, 3},
                {2, 0, 0},
                {3, 0, 0}
        };
        Region subgridRegion = board.getSubgridRegion(0, 0);
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(subgridValues[row][col], subgridRegion.getCells().get(index).getNumber(),
                        "Subgrid region should match the set values");
                index++;
            }
        }
    }
}
