package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class NumberPadView extends JPanel {
    private final List<NumberButtonView> numberButtons = new ArrayList<>();

    public NumberPadView() {
        super(new GridLayout(3, 3));
        buildNumberPad();
    }

    private void buildNumberPad() {
        for (int i = 1; i <= 9; i++) {
            NumberButtonView button = new NumberButtonView(i);

            // Add to list for ref
            numberButtons.add(button);

            // Add to panel for visibility
            add(button);
        }
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
    }

    public void addNumberButtonListener(IntConsumer listener) {
        for (NumberButtonView button : numberButtons) {
            button.addActionListener(e -> listener.accept(button.getNumber()));
        }
    }

    public void resetButtons() {
        for (NumberButtonView button : numberButtons) {
            button.setFocusPainted(false);
        }
    }

    public void focusButton(int number) {
        numberButtons.get(number - 1).setFocusPainted(true);
    }

    public void enablePad() {
        for (NumberButtonView button : numberButtons) {
            button.setFocusPainted(false);
            button.setEnabled(true);
        }
    }
}
