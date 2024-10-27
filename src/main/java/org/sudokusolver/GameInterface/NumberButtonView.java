package org.sudokusolver.GameInterface;

import javax.swing.*;

public class NumberButtonView extends JButton {
    private final int number;

    public NumberButtonView(int number) {
        super(String.valueOf(number));
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
