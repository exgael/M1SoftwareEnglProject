package org.sudokusolver.GameInterface;

import javax.swing.*;
import java.awt.*;

public class SudokuView extends JFrame {
    private final ControlPanelView controls;
    private final NumberPadView numberPad;
    private final SudokuBoardView boardView;

    public SudokuView(SudokuController controller) {
        super("Sudoku");
        controller.setSudokuView(this);

        // Set Sub views
        controls = new ControlPanelView(controller);
        numberPad = new NumberPadView(controller);
        boardView = new SudokuBoardView(controller);

        buildView();
    }

    public void updateCell(int row, int col, int value) {
        boardView.updateCell(row, col, value);
    }

    public void focusPadButton(int i) {
        numberPad.focusButton(i);
    }

    public void resetPad() {
        numberPad.resetButtons();
    }

    public void enablePad() {
        numberPad.enablePad();
    }

    public void updateControlButtons(boolean isGridLoaded) {
        controls.setControlButtonsEnabled(isGridLoaded);
    }

    private void buildView() {
        setSize(600, 400);
        setLocation(200, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //container de base
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 2));
        //left side
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(boardView, BorderLayout.CENTER);

        //right side
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(controls);
        rightPanel.add(numberPad, BorderLayout.CENTER);
        rightPanel.add(southPanel, BorderLayout.SOUTH);

        pane.add(leftPanel);
        pane.add(rightPanel);

        setVisible(true);
    }
}
