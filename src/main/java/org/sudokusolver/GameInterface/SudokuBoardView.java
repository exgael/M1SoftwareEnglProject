package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class SudokuBoardView extends JPanel {

    private final SudokuController controller;
    private final CellView[][] cells = new CellView[9][9];

    public SudokuBoardView(SudokuController controller) {
        super(new GridLayout(9, 9));
        this.controller = controller;
        buildView();
    }

    /**
     * Update the visual of a cell in the board
     * @param row row of the cell
     * @param col column of the cell
     * @param value new value the cell must take on
     */
    public void updateCell(int row, int col, int value) {
        cells[row][col].setValue(value);
    }

    private void buildView() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                CellView cell = new CellView(controller, row, col);

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
}
