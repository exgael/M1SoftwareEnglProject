package org.sudokusolver.Strategy.Solver.Regions;

import org.sudokusolver.Gameplay.Sudoku;
import org.sudokusolver.Strategy.Sudoku.SudokuCell;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegionManager {

    private final List<Region> rows;
    private final List<Region> columns;
    private final List<Region> subgrids;

    private final int sudokuSize;
    private final int subgridSize;

    public RegionManager(Sudoku sudoku) {
        this.sudokuSize = sudoku.getBoardSize();
        this.subgridSize = sudoku.getSubgridSize();
        this.rows = new ArrayList<>(sudokuSize);
        this.columns = new ArrayList<>(sudokuSize);
        this.subgrids = new ArrayList<>(sudokuSize);
        initializeRegions(sudoku);
    }

    /**
     * Initializes the regions of the Sudoku board.
     *
     * @param sudoku The Sudoku board.
     */
    private void initializeRegions(Sudoku sudoku) {
        for (int i = 0; i < sudokuSize; i++) {
            // Collections of cells
            List<SudokuCell> rowCells = getRowCells(sudoku, i);
            List<SudokuCell> columnCells = getColumnCells(sudoku, i);
            int startRow = (i / subgridSize) * subgridSize;
            int startCol = (i % subgridSize) * subgridSize;
            List<SudokuCell> subgridCells = getCellsInSubgrid(sudoku, startRow, startCol);

            // Add the initialized regions
            this.rows.add(new Region(rowCells));
            this.columns.add(new Region(columnCells));
            this.subgrids.add(new Region(subgridCells));
        }
    }

    /**
     * Return a stream of all the regions.
     *
     * @return The stream of regions.
     */
    public Stream<Region> stream() {
        return Stream.of(rows, columns, subgrids).flatMap(Collection::stream);
    }

    // /////////////////////////////////////////
    // Helper methods to initialize regions   //
    // /////////////////////////////////////////

    /**
     * Returns a list of cells in a row.
     *
     * @param board The Sudoku board.
     * @param row   The row index.
     * @return List of cells.
     */
    private List<SudokuCell> getRowCells(Sudoku board, int row) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(col -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a column.
     *
     * @param board The Sudoku board.
     * @param col   The column index.
     * @return List of cells.
     */
    private List<SudokuCell> getColumnCells(Sudoku board, int col) {
        return IntStream.range(0, sudokuSize)
                .mapToObj(row -> board.getElement(row, col))
                .toList();
    }

    /**
     * Returns a list of cells in a subgrid.
     *
     * @param board    The Sudoku board.
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @return List of cells.
     */
    private List<SudokuCell> getCellsInSubgrid(Sudoku board, int startRow, int startCol) {
        List<SudokuCell> cells = new ArrayList<>();
        iterateSubgrid(startRow, startCol, (r, c) ->
                cells.add(board.getElement(r, c))
        );
        return cells;
    }

    /**
     * Executes an action for each cell in a subgrid.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @param action   The action to be performed.
     */
    private void iterateSubgrid(int startRow, int startCol, BiConsumer<Integer, Integer> action) {
        for (int rOffset = 0; rOffset < subgridSize; rOffset++) {
            for (int cOffset = 0; cOffset < subgridSize; cOffset++) {
                action.accept(startRow + rOffset, startCol + cOffset);
            }
        }
    }
}
