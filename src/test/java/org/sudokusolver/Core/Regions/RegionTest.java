//package org.sudokusolver.Core.Regions;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.sudokusolver.Core.SudokuCell;
//import org.sudokusolver.Solver.Regions.Coordinate;
//import org.sudokusolver.Solver.Regions.Region;
//
//import java.util.*;
//import java.util.function.Consumer;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class RegionTest {
//
//    private Region region;
//    private List<SudokuCell> cells;
//    private Map<SudokuCell, Coordinate> coordinates;
//
//    @BeforeEach
//    void setUp() {
//        cells = Arrays.asList(
//                new SudokuCell(), new SudokuCell(), new SudokuCell(),
//                new SudokuCell(), new SudokuCell(), new SudokuCell(),
//                new SudokuCell(), new SudokuCell(), new SudokuCell()
//        );
//
//        coordinates = new HashMap<>();
//        for (int i = 0; i < cells.size(); i++) {
//            coordinates.put(cells.get(i), new Coordinate(i, i));
//        }
//        region = new Region(cells, coordinates);
//    }
//
//    @Test
//    @DisplayName("getCells should return the list of cells")
//    void testGetCells() {
//        assertEquals(cells, region.getCells(), "getCells should return the list of cells");
//    }
//
//    @Test
//    @DisplayName("containsValue should return true if value is in the region")
//    void testContainsValueTrue() {
//        cells.get(0).setNumber(5);
//        assertTrue(region.containsValue(5), "containsValue should return true if value is in the region");
//    }
//
//    @Test
//    @DisplayName("containsValue should return false if value is not in the region")
//    void testContainsValueFalse() {
//        cells.get(0).setNumber(5);
//        assertFalse(region.containsValue(3), "containsValue should return false if value is not in the region");
//    }
//
//    @Test
//    @DisplayName("isSolved should return true when all cells are solved")
//    void testIsSolvedTrue() {
//        for (int i = 0; i < cells.size(); i++) {
//            cells.get(i).setNumber(i + 1);
//        }
//        assertTrue(region.isSolved(), "isSolved should return true when all cells are solved");
//    }
//
//    @Test
//    @DisplayName("isSolved should return false when not all cells are solved")
//    void testIsSolvedFalse() {
//        for (int i = 0; i < cells.size() - 1; i++) {
//            cells.get(i).setNumber(i + 1);
//        }
//        assertFalse(region.isSolved(), "isSolved should return false when not all cells are solved");
//    }
//
//    @Test
//    @DisplayName("removeCandidate should remove candidate from all cells")
//    void testRemoveCandidate() {
//        cells.forEach(cell -> cell.initializeCandidates(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
//        region.removeCandidate(5);
//        cells.forEach(cell -> assertFalse(cell.getCandidates().contains(5), "Candidate 5 should be removed from all cells"));
//    }
//
//    @Test
//    @DisplayName("findCellsWithCandidateCount should return cells with specified candidate count")
//    void testFindCellsWithCandidateCount() {
//        cells.forEach(cell -> cell.initializeCandidates(Arrays.asList(1, 2, 3)));
//
//        cells.get(0).removeCandidate(1); // Now has 2 candidates
//        cells.get(1).removeCandidate(1);
//        cells.get(1).removeCandidate(2); // Now has 1 candidate
//
//        List<SudokuCell> result = region.findCellsWithCandidateCount(2);
//        assertEquals(1, result.size(), "Should find one cell with 2 candidates");
//        assertTrue(result.contains(cells.get(0)), "Should contain the first cell");
//
//        result = region.findCellsWithCandidateCount(1);
//        assertEquals(1, result.size(), "Should find one cell with 1 candidate");
//        assertTrue(result.contains(cells.get(1)), "Should contain the second cell");
//    }
//
//    @Test
//    @DisplayName("findUnsolvedCells should return all unsolved cells")
//    void testFindUnsolvedCells() {
//        cells.get(0).setNumber(5);
//        List<SudokuCell> unsolvedCells = region.findUnsolvedCells();
//        assertEquals(cells.size() - 1, unsolvedCells.size(), "Should return all unsolved cells");
//        assertFalse(unsolvedCells.contains(cells.get(0)), "Should not contain the solved cell");
//    }
//
//    @Test
//    @DisplayName("removeCandidate with exclusion should not remove candidate from excluded cells")
//    void testRemoveCandidateWithExclusions() {
//        cells.forEach(cell -> cell.initializeCandidates(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
//        Set<SudokuCell> excludeCells = new HashSet<>();
//        excludeCells.add(cells.get(0));
//        excludeCells.add(cells.get(1));
//
//        region.removeCandidate(5, excludeCells);
//
//        for (int i = 2; i < cells.size(); i++) {
//            assertFalse(cells.get(i).getCandidates().contains(5), "Candidate 5 should be removed from cell " + i);
//        }
//        assertTrue(cells.get(0).getCandidates().contains(5), "Candidate 5 should not be removed from excluded cell 0");
//        assertTrue(cells.get(1).getCandidates().contains(5), "Candidate 5 should not be removed from excluded cell 1");
//    }
//
//    @Test
//    @DisplayName("removeCandidates with exclusion should not remove candidates from excluded cells")
//    void testRemoveCandidatesWithExclusions() {
//        cells.forEach(cell -> cell.initializeCandidates(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
//        Set<SudokuCell> excludeCells = new HashSet<>();
//        excludeCells.add(cells.get(0));
//        excludeCells.add(cells.get(1));
//
//        Set<Integer> candidatesToRemove = new HashSet<>(Arrays.asList(5, 6, 7));
//
//        region.removeCandidates(candidatesToRemove, excludeCells);
//
//        for (int i = 2; i < cells.size(); i++) {
//            assertFalse(cells.get(i).getCandidates().contains(5), "Candidate 5 should be removed from cell " + i);
//            assertFalse(cells.get(i).getCandidates().contains(6), "Candidate 6 should be removed from cell " + i);
//            assertFalse(cells.get(i).getCandidates().contains(7), "Candidate 7 should be removed from cell " + i);
//        }
//        assertTrue(cells.get(0).getCandidates().contains(5), "Candidate 5 should not be removed from excluded cell 0");
//        assertTrue(cells.get(1).getCandidates().contains(6), "Candidate 6 should not be removed from excluded cell 1");
//    }
//
//    @Test
//    @DisplayName("forEach should apply action to all cells")
//    void testForEach() {
//        SudokuCell cellSpy1 = spy(new SudokuCell());
//        SudokuCell cellSpy2 = spy(new SudokuCell());
//        SudokuCell cellSpy3 = spy(new SudokuCell());
//        List<SudokuCell> spyCells = Arrays.asList(cellSpy1, cellSpy2, cellSpy3);
//        region = new Region(spyCells);
//
//        Consumer<SudokuCell> action = cell -> cell.setNumber(5);
//
//        region.forEach(action);
//
//        spyCells.forEach(cell -> verify(cell).setNumber(5));
//    }
//}
