package org.sudokusolver.Strategy.Sudoku;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Utils.Inspectable;

import java.util.ArrayList;
import java.util.List;

public class Board<T extends Inspectable> {

    private final List<List<T>> grid;

    private final int rows;

    private final int cols;

    public Board(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Board size must be positive.");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new ArrayList<>(rows);
        this.initNullBoard();
    }


    private void initNullBoard() {
        for (int row = 0; row < rows; row++) {
            List<T> rowList = new ArrayList<>(cols);
            for (int col = 0; col < cols; col++) {
                rowList.add(null);
            }
            grid.add(rowList);
        }
    }


    public T getElement(int row, int col) {
        validateCoordinates(row, col);
        return grid.get(row).get(col);
    }


    public void setElement(int row, int col, T value) {
        validateCoordinates(row, col);
        grid.get(row).set(col, value);
    }


    public int getRows() {
        return rows;
    }


    public int getCols() {
        return cols;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                appendValue(row, col, sb);
            }
            addNewLine(sb);
        }
        return sb.toString();
    }


    private void appendValue(int row, int col, @NotNull StringBuilder sb) {
        sb.append(this.getElement(row, col)).append(" ");
    }


    private void addNewLine(@NotNull StringBuilder sb) {
        sb.append("\n");
    }


    private void validateCoordinates(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Invalid board coordinates: (" + row + ", " + col + ")");
        }
    }
}