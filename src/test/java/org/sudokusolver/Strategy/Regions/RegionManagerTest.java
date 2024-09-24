package org.sudokusolver.Strategy.Regions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Gameplay.SudokuCell;
import org.sudokusolver.Utils.SudokuGrids;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RegionManagerTest {

    private SudokuBoard board;
    private RegionManager regionManager;
    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(SudokuGrids.hardSudoku);
        regionManager = new RegionManager(board);
    }

    @Test
    void testRegionManagerInitialization() {
        assertNotNull(regionManager);
        assertEquals(BOARD_SIZE, regionManager.getRowRegion(0).getCells().size());
        assertEquals(BOARD_SIZE, regionManager.getColumnRegion(0).getCells().size());
        assertEquals(BOARD_SIZE, regionManager.getSubgridRegion(0).getCells().size());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0",
            "0, 1, 9",
            "4, 4, 9",
            "8, 8, 6"
    })
    void testGetValue(int row, int col, int expectedValue) {
        assertEquals(expectedValue, regionManager.getValue(row, col));
        assertEquals(board.getValue(row, col), regionManager.getValue(row, col));
    }

    @Test
    void testSetValue() {
        int row = 0, col = 0, value = 1;
        regionManager.setValue(row, col, value);
        assertEquals(value, regionManager.getValue(row, col));
        assertEquals(value, board.getValue(row, col));
    }

    @Test
    void testCanPlaceValue() {
        assertTrue(regionManager.canPlaceValue(0, 0, 1));
        assertFalse(regionManager.canPlaceValue(0, 1, 9)); // 9 is already in this cell
    }

    @Test
    void testGetPossibleValues() {
        List<Integer> possibleValues = regionManager.getPossibleValues(0, 0);
        assertFalse(possibleValues.isEmpty());
        assertFalse(possibleValues.contains(9)); // 9 is already in the first row
    }

    @Test
    void testRegionIntegrity() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            verifyRegionIntegrity(regionManager.getRowRegion(i), i, true);
            verifyRegionIntegrity(regionManager.getColumnRegion(i), i, false);
            verifyRegionIntegrity(regionManager.getSubgridRegion(i), i, null);
        }
    }

    private void verifyRegionIntegrity(Region region, int index, Boolean isRow) {
        Set<SudokuCell> uniqueCells = new HashSet<>();
        for (int j = 0; j < BOARD_SIZE; j++) {
            SudokuCell cell = region.getCells().get(j);
            Coordinate coordinate = region.getCellCoordinate(cell);

            // Verify coordinate correctness
            if (isRow != null) {
                if (isRow) {
                    assertEquals(index, coordinate.row());
                    assertEquals(j, coordinate.column());
                } else {
                    assertEquals(j, coordinate.row());
                    assertEquals(index, coordinate.column());
                }
            } else {
                // For subgrid
                int expectedRow = (index / SUBGRID_SIZE) * SUBGRID_SIZE + (j / SUBGRID_SIZE);
                int expectedCol = (index % SUBGRID_SIZE) * SUBGRID_SIZE + (j % SUBGRID_SIZE);
                assertEquals(expectedRow, coordinate.row());
                assertEquals(expectedCol, coordinate.column());
            }

            // Verify cell value matches the board
            assertEquals(board.getValue(coordinate.row(), coordinate.column()), cell.getNumber());

            // Ensure cell uniqueness within the region
            assertTrue(uniqueCells.add(cell), "Duplicate cell found in region");
        }
    }

    @Test
    void testCrossRegionIntegrity() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SudokuCell rowCell = regionManager.getRowRegion(row).getCells().get(col);
                SudokuCell colCell = regionManager.getColumnRegion(col).getCells().get(row);
                SudokuCell subgridCell = regionManager.getSubgridRegion((row / SUBGRID_SIZE) * SUBGRID_SIZE + (col / SUBGRID_SIZE))
                        .getCells().get((row % SUBGRID_SIZE) * SUBGRID_SIZE + (col % SUBGRID_SIZE));

                // All these cells should be the same object
                assertSame(rowCell, colCell);
                assertSame(rowCell, subgridCell);

                // Verify that the cell value matches the board
                assertEquals(board.getValue(row, col), rowCell.getNumber());
            }
        }
    }

    @Test
    void testSetValueUpdatesAllRegions() {
        int row = 0, col = 0, value = 1;
        regionManager.setValue(row, col, value);

        // Check row
        assertEquals(value, regionManager.getRowRegion(row).getCells().get(col).getNumber());

        // Check column
        assertEquals(value, regionManager.getColumnRegion(col).getCells().get(row).getNumber());

        // Check subgrid
        int subgridIndex = (row / SUBGRID_SIZE) * SUBGRID_SIZE + (col / SUBGRID_SIZE);
        int cellIndexInSubgrid = (row % SUBGRID_SIZE) * SUBGRID_SIZE + (col % SUBGRID_SIZE);
        assertEquals(value, regionManager.getSubgridRegion(subgridIndex).getCells().get(cellIndexInSubgrid).getNumber());

        // Check that the value is removed from candidates in related cells
        IntStream.range(0, BOARD_SIZE).forEach(i -> {
            if (i != col) assertFalse(regionManager.getRowRegion(row).getCells().get(i).getCandidates().contains(value));
            if (i != row) assertFalse(regionManager.getColumnRegion(col).getCells().get(i).getCandidates().contains(value));
        });
    }

    @Test
    void testStream() {
        long totalRegions = regionManager.stream().count();
        assertEquals(BOARD_SIZE * 3, totalRegions); // 9 rows + 9 columns + 9 subgrids
    }

    @Test
    void testForEachRegion() {
        Set<Region> allRegions = new HashSet<>();
        regionManager.forEachRegion(allRegions::add);
        assertEquals(BOARD_SIZE * 3, allRegions.size());
    }

    @Test
    void testSetValueUpdatesCandidates() {
        int row = 0, col = 0, value = 1;

        // Store initial candidates
        Map<Coordinate, Set<Integer>> initialCandidates = storeInitialCandidates();

        regionManager.setValue(row, col, value);

        // Check row
        for (int c = 0; c < BOARD_SIZE; c++) {
            if (c != col) {
                assertFalse(regionManager.getRowRegion(row).getCells().get(c).getCandidates().contains(value),
                        "Candidate " + value + " should be removed from cell (" + row + "," + c + ")");
            }
        }

        // Check column
        for (int r = 0; r < BOARD_SIZE; r++) {
            if (r != row) {
                assertFalse(regionManager.getColumnRegion(col).getCells().get(r).getCandidates().contains(value),
                        "Candidate " + value + " should be removed from cell (" + r + "," + col + ")");
            }
        }

        // Check subgrid
        int subgridRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int subgridCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        for (int r = subgridRow; r < subgridRow + SUBGRID_SIZE; r++) {
            for (int c = subgridCol; c < subgridCol + SUBGRID_SIZE; c++) {
                if (r != row || c != col) {
                    assertFalse(regionManager.getSubgridRegion((r / SUBGRID_SIZE) * SUBGRID_SIZE + (c / SUBGRID_SIZE))
                                    .getCells().get((r % SUBGRID_SIZE) * SUBGRID_SIZE + (c % SUBGRID_SIZE)).getCandidates().contains(value),
                            "Candidate " + value + " should be removed from cell (" + r + "," + c + ")");
                }
            }
        }

        // Verify that unrelated cells' candidates remain unchanged
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (r != row && c != col && (r / SUBGRID_SIZE != row / SUBGRID_SIZE || c / SUBGRID_SIZE != col / SUBGRID_SIZE)) {
                    assertEquals(initialCandidates.get(new Coordinate(r, c)),
                            regionManager.getRowRegion(r).getCells().get(c).getCandidates(),
                            "Candidates for unrelated cell (" + r + "," + c + ") should remain unchanged");
                }
            }
        }
    }

    @Test
    void testSetValueRemovesCandidatesFromCell() {
        int row = 0, col = 0, value = 1;
        SudokuCell cell = regionManager.getRowRegion(row).getCells().get(col);
        Set<Integer> initialCandidates = new HashSet<>(cell.getCandidates());

        regionManager.setValue(row, col, value);

        assertTrue(cell.getCandidates().isEmpty(), "Cell candidates should be empty after setting a value");
        assertNotEquals(initialCandidates, cell.getCandidates(), "Cell candidates should change after setting a value");
    }

    @Test
    void testSetValueConsistencyAcrossRegions() {
        int row = 0, col = 0, value = 1;
        regionManager.setValue(row, col, value);

        SudokuCell rowCell = regionManager.getRowRegion(row).getCells().get(col);
        SudokuCell colCell = regionManager.getColumnRegion(col).getCells().get(row);
        SudokuCell subgridCell = regionManager.getSubgridRegion((row / SUBGRID_SIZE) * SUBGRID_SIZE + (col / SUBGRID_SIZE))
                .getCells().get((row % SUBGRID_SIZE) * SUBGRID_SIZE + (col % SUBGRID_SIZE));

        assertEquals(value, rowCell.getNumber());
        assertEquals(value, colCell.getNumber());
        assertEquals(value, subgridCell.getNumber());

        assertTrue(rowCell.getCandidates().isEmpty());
        assertTrue(colCell.getCandidates().isEmpty());
        assertTrue(subgridCell.getCandidates().isEmpty());
    }

    @Test
    void testGetPossibleValuesAfterSetValue() {
        int row = 0, col = 0, value = 1;
        regionManager.setValue(row, col, value);

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                List<Integer> possibleValues = regionManager.getPossibleValues(r, c);
                if (r == row && c == col) {
                    assertTrue(possibleValues.isEmpty(), "Possible values for set cell should be empty");
                } else if (r == row || c == col ||
                        (r / SUBGRID_SIZE == row / SUBGRID_SIZE && c / SUBGRID_SIZE == col / SUBGRID_SIZE)) {
                    assertFalse(possibleValues.contains(value),
                            "Value " + value + " should not be possible for cell (" + r + "," + c + ")");
                }
            }
        }
    }

    private Map<Coordinate, Set<Integer>> storeInitialCandidates() {
        Map<Coordinate, Set<Integer>> initialCandidates = new HashMap<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                initialCandidates.put(new Coordinate(r, c),
                        new HashSet<>(regionManager.getRowRegion(r).getCells().get(c).getCandidates()));
            }
        }
        return initialCandidates;
    }
}