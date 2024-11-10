package org.sudokusolver.GameInterface;

import javax.swing.*;

public class NumberButtonView extends JButton {
    private final int number;

    public NumberButtonView(SudokuController c, int number) {
        super(String.valueOf(number));
        this.number = number;
        this.addActionListener(e -> c.handleNumPadClick(number));
    }

    public int getNumber() {
        return number;
    }
}
