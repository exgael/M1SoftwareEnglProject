package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class CellView extends JLabel {
    private final int row;
    private final int col;

    public CellView(int row, int col) {
        super("");
        this.row = row;
        this.col = col;

        setHorizontalAlignment(JTextField.CENTER);
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 18));
    }

    public void setBorders(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
    }

    public void setValue(int value) {
        setText(value == 0 ? "" : String.valueOf(value));
    }

    public void addCellClickListener(MouseAdapter listener) {
        addMouseListener(listener);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
