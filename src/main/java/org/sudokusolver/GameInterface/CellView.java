package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class CellView extends JButton {
    private final int row;
    private final int col;

    public CellView(SudokuController controller, int row, int col) {
        super("");
        this.row = row;
        this.col = col;

        buildView();
        addAction(controller);
    }

    public void setBorders(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
    }

    /**
     * Update the visual of a cell
     * @param value new value the cell must take on
     */
    public void setValue(int value) {
        setText(value == 0 ? "" : String.valueOf(value));
    }

    private void buildView() {
        setHorizontalAlignment(JTextField.CENTER);
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 18));
    }

    private void addAction(SudokuController controller) {
        this.addActionListener(e -> controller.handlePlayMove(row, col));
    }


}
