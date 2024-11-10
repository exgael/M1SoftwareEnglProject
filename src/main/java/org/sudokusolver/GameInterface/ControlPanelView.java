package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JPanel {
    private final JButton loadGridButton = new JButton("Load Grid");
    private final JButton solveButton = new JButton("Solve");
    private final JButton resetButton = new JButton("Reset");

    public ControlPanelView(SudokuController controller) {
        super(new FlowLayout(FlowLayout.CENTER));
        buildView();
        addAction(controller);
    }

    public void setControlButtonsEnabled(boolean enabled) {
        solveButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
    }

    private void buildView() {
        add(loadGridButton);
        add(solveButton);
        add(resetButton);
        setControlButtonsEnabled(false);
    }

    private void addAction(SudokuController controller) {
        loadGridButton.addActionListener(e -> controller.handleLoadGridClick());
        solveButton.addActionListener(e -> controller.handleSolveClicked());
        resetButton.addActionListener(e -> controller.handleResetClicked());
    }
}
