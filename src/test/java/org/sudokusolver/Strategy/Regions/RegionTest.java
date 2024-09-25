package org.sudokusolver.Strategy.Regions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Gameplay.SudokuCell;
import org.sudokusolver.Utils.SudokuGrids;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {

    private SudokuBoard board;
    private Region rowRegion;
    private Region columnRegion;
    private Region subgridRegion;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(SudokuGrids.hardSudoku);
        rowRegion = createRowRegion(0);
        columnRegion = createColumnRegion(0);
        subgridRegion = createSubgridRegion(0, 0);
    }

    @Test
    void testGetCells() {
        assertEquals(9, rowRegion.getCells().size());
        assertEquals(9, columnRegion.getCells().size());
        assertEquals(9, subgridRegion.getCells().size());
    }

    @Test
    void testContainsValue() {
        assertTrue(rowRegion.containsValue(9));
        assertFalse(rowRegion.containsValue(1));

        assertTrue(columnRegion.containsValue(3));
        assertFalse(columnRegion.containsValue(1));

        assertTrue(subgridRegion.containsValue(9));
        assertFalse(subgridRegion.containsValue(1));
    }

    @Test
    void testGetCellCoordinate() {
        SudokuCell cell = rowRegion.getCells().get(0);
        assertEquals(new Coordinate(0, 0), rowRegion.getCellCoordinate(cell));

        cell = columnRegion.getCells().get(1);
        assertEquals(new Coordinate(1, 0), columnRegion.getCellCoordinate(cell));

        cell = subgridRegion.getCells().get(4);
        assertEquals(new Coordinate(1, 1), subgridRegion.getCellCoordinate(cell));
    }

    @Test
    void testIsSolved() {
        assertFalse(rowRegion.isSolved());
        assertFalse(columnRegion.isSolved());
        assertFalse(subgridRegion.isSolved());

        // Create a solved region
        List<SudokuCell> solvedCells = IntStream.rangeClosed(1, 9)
                .mapToObj(i -> {
                    SudokuCell cell = new SudokuCell();
                    cell.setNumber(i);
                    return cell;
                })
                .collect(Collectors.toList());
        Map<SudokuCell, Coordinate> coordinates = IntStream.range(0, 9)
                .boxed()
                .collect(Collectors.toMap(
                        solvedCells::get,
                        i -> new Coordinate(0, i)
                ));
        Region solvedRegion = new Region(solvedCells, coordinates);
        assertTrue(solvedRegion.isSolved());
    }

    @Test
    void testFindCellsWithCandidateCount() {
        List<SudokuCell> cellsWithTwoCandidates = rowRegion.findCellsWithCandidateCount(2);
        assertFalse(cellsWithTwoCandidates.isEmpty());
        cellsWithTwoCandidates.forEach(cell -> assertEquals(2, cell.candidateCount()));
    }

    @Test
    void testFindUnsolvedCells() {
        Set<SudokuCell> unsolvedCells = rowRegion.findUnsolvedCells();
        assertFalse(unsolvedCells.isEmpty());
        unsolvedCells.forEach(cell -> assertFalse(cell.isSolved()));
    }

    @Test
    void testRemoveCandidateFromAll() {
        int candidateToRemove = 5;
        rowRegion.removeCandidateFromAll(candidateToRemove);
        rowRegion.getCells().forEach(cell -> assertFalse(cell.getCandidates().contains(candidateToRemove)));
    }

    @Test
    void testRemoveCandidates() {
        Set<Integer> candidatesToRemove = Set.of(1, 2, 3);
        Set<SudokuCell> excludeCells = new HashSet<>(rowRegion.getCells().subList(0, 3));

        // Store the initial state of candidates
        Map<SudokuCell, Set<Integer>> initialCandidates = rowRegion.getCells().stream()
                .collect(Collectors.toMap(
                        cell -> cell,
                        cell -> new HashSet<>(cell.getCandidates())
                ));

        rowRegion.removeCandidates(candidatesToRemove, excludeCells);

        for (int i = 0; i < rowRegion.getCells().size(); i++) {
            SudokuCell cell = rowRegion.getCells().get(i);
            Set<Integer> initialCellCandidates = initialCandidates.get(cell);

            if (i < 3) {
                // Excluded cells should have the same candidates as before
                assertEquals(initialCellCandidates, cell.getCandidates());
            } else {
                // Non-excluded cells should not have the candidates that were initially present
                Set<Integer> removedCandidates = initialCellCandidates.stream()
                        .filter(candidatesToRemove::contains)
                        .collect(Collectors.toSet());
                assertTrue(Collections.disjoint(cell.getCandidates(), removedCandidates));
            }
        }
    }

    @Test
    void testRemoveCandidate() {
        int candidateToRemove = 4;
        Set<SudokuCell> excludeCells = new HashSet<>(rowRegion.getCells().subList(0, 3));

        // Store the initial state of the candidate
        Map<SudokuCell, Boolean> initialCandidatePresence = rowRegion.getCells().stream()
                .collect(Collectors.toMap(
                        cell -> cell,
                        cell -> cell.getCandidates().contains(candidateToRemove)
                ));

        rowRegion.removeCandidate(candidateToRemove, excludeCells);

        for (int i = 0; i < rowRegion.getCells().size(); i++) {
            SudokuCell cell = rowRegion.getCells().get(i);
            boolean initiallyPresent = initialCandidatePresence.get(cell);

            if (i < 3) {
                // Excluded cells should have the same state for the candidate as before
                assertEquals(initiallyPresent, cell.getCandidates().contains(candidateToRemove));
            } else {
                // Non-excluded cells should not have the candidate if it was initially present
                assertFalse(cell.getCandidates().contains(candidateToRemove));
            }
        }
    }

    @Test
    void testForEach() {
        List<Integer> values = new ArrayList<>();
        rowRegion.forEach(cell -> values.add(cell.getNumber()));
        assertEquals(Arrays.asList(0, 9, 0, 6, 8, 0, 2, 5, 0), values);
    }

    private Region createRowRegion(int row) {
        List<SudokuCell> cells = IntStream.range(0, 9)
                .mapToObj(col -> board.getElement(row, col))
                .collect(Collectors.toList());
        Map<SudokuCell, Coordinate> coordinates = IntStream.range(0, 9)
                .boxed()
                .collect(Collectors.toMap(
                        cells::get,
                        col -> new Coordinate(row, col)
                ));
        return new Region(cells, coordinates);
    }

    private Region createColumnRegion(int col) {
        List<SudokuCell> cells = IntStream.range(0, 9)
                .mapToObj(row -> board.getElement(row, col))
                .collect(Collectors.toList());
        Map<SudokuCell, Coordinate> coordinates = IntStream.range(0, 9)
                .boxed()
                .collect(Collectors.toMap(
                        cells::get,
                        row -> new Coordinate(row, col)
                ));
        return new Region(cells, coordinates);
    }

    private Region createSubgridRegion(int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        Map<SudokuCell, Coordinate> coordinates = new HashMap<>();
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                SudokuCell cell = board.getElement(row, col);
                cells.add(cell);
                coordinates.put(cell, new Coordinate(row, col));
            }
        }
        return new Region(cells, coordinates);
    }
}