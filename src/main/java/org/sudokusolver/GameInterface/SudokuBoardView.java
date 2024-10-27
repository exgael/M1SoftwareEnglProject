package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class SudokuBoardView extends JPanel {
    private final CellView[][] cells = new CellView[9][9];

    public SudokuBoardView() {
        super(new GridLayout(9, 9));
        buildBoard();
    }

    private void buildBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                CellView cell = new CellView(row, col);

                // Borders
                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : 1;
                int right = (col == 8) ? 2 : 1;
                cell.setBorders(top, left, bottom, right);

                cells[row][col] = cell;
                add(cell);
            }
        }
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public CellView getCell(int row, int col) {
        return cells[row][col];
    }

    public void addCellClickListener(MouseAdapter listener) {
        for (CellView[] rowCells : cells) {
            for (CellView cell : rowCells) {
                cell.addCellClickListener(listener);
            }
        }
    }

    public void updateCell(int row, int col, int value) {
        cells[row][col].setValue(value);
    }
}
