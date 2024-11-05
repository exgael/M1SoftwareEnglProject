package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JPanel {
    private final JButton startButton = new JButton("Start");
    private final JButton solveButton = new JButton("Solve");

    public ControlPanelView(SudokuController controller) {
        super(new FlowLayout(FlowLayout.CENTER));
        buildView();
        addAction(controller);
    }

    private void buildView() {
        add(startButton);
        add(solveButton);
    }

    private void addAction(SudokuController controller) {
        startButton.addActionListener(e -> controller.handleStartClicked());
        solveButton.addActionListener(e -> controller.handleSolveClicked());
    }
}
