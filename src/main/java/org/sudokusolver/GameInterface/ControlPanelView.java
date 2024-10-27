package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JPanel {
    private final JButton startButton = new JButton("Start");
    private final JButton solveButton = new JButton("Solve");

    public ControlPanelView() {
        super(new FlowLayout(FlowLayout.CENTER));
        add(startButton);
        add(solveButton);
    }

    public void addStartButtonListener(java.awt.event.ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addSolveButtonListener(java.awt.event.ActionListener listener) {
        solveButton.addActionListener(listener);
    }
}
