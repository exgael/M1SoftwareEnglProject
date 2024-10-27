package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class SudokuView extends JFrame implements GameInterface {
    private final ControlPanelView controls;
    private final NumberPadView numberPad;
    private final SudokuBoardView boardView;

    public SudokuView() {
        super("Sudoku");
        controls = new ControlPanelView();
        numberPad = new NumberPadView();
        boardView = new SudokuBoardView();
        buildView();
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

    public void addCellClickListener(BiConsumer<Integer, Integer> listener) {
        boardView.addCellClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CellView cell = (CellView) e.getSource();
                SwingUtilities.invokeLater(() -> listener.accept(cell.getRow(), cell.getCol()));

                // On mouse click of boardView, reset Number pad
                SwingUtilities.invokeLater(numberPad::resetButtons);
            }
        });
    }

    public void addNumpadClickListener(IntConsumer listener) {
        numberPad.addNumberButtonListener(listener);
    }

    public void addStartButtonListener(ActionListener listener) {
        controls.addStartButtonListener(listener);
    }

    public void addSolveButtonListener(ActionListener listener) {
        controls.addSolveButtonListener(listener);
    }

    @Override
    public void onRequestUserInput() {
        JOptionPane.showMessageDialog(this, "Help me!");
        SwingUtilities.invokeLater(numberPad::enablePad);
    }

    @Override
    public void onSudokuFinished(int level) {
        JOptionPane.showMessageDialog(this, "Level difficulty is : "+levelRep(level), "End of solver", JOptionPane.PLAIN_MESSAGE);
    }

    private String levelRep(int dif) {
        return switch (dif) {
            case 0 -> " Easy :)";
            case 1 -> " Average :|";
            case 2 -> " Hard :(";
            default -> " Impossible ?x_x?";
        };
    }

    @Override
    public void onInvalidMove(int value, int row, int col) {
        JOptionPane.showMessageDialog(this, "Conflicting values found! Game Over!");
    }

    @Override
    public void update(SudokuUpdate data) {
        updateCell(data.row(), data.col(), data.value());
    }

    public void updateCell(int row, int col, int value) {
        SwingUtilities.invokeLater(() -> boardView.updateCell(row, col, value));
    }

    public void focusPadButton(int i) {
        SwingUtilities.invokeLater(() -> numberPad.focusButton(i));
    }

    public void resetPad() {
        SwingUtilities.invokeLater(numberPad::resetButtons);
    }
}
