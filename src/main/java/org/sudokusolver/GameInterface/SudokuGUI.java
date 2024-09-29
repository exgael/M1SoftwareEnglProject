package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Strategy.Solver.DifficultyLevel;
import org.sudokusolver.Strategy.Sudoku.SudokuCellUpdate;
import org.sudokusolver.Gameplay.UserMove;
import org.sudokusolver.Utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGUI extends JFrame implements ActionListener, Observer<SudokuCellUpdate>, GameInterface {

    private final JButton solveButton;
    private final JButton resetButton;
    private final JButton[] numberButtons;
    private final JLabel[][] cells;
    private final GameEngine engine;

    public SudokuGUI(GameEngine engine) {
        super("Sudoku Solver");
        this.engine = engine;
        this.engine.setGameInterface(this);
        this.engine.addListener(this);

        setSize(600, 400);
        setLocation(200, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //container de base
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 2));

        //left side
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel sudokuPanel = new JPanel(new GridLayout(9, 9));
        cells = new JLabel[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JLabel("0");
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);

                // borders
                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : 1;
                int right = (col == 8) ? 2 : 1;

                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                sudokuPanel.add(cells[row][col]);
            }
        }
        sudokuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(sudokuPanel, BorderLayout.CENTER);

        //right side
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel numberPanel = new JPanel(new GridLayout(3, 3));
        numberButtons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            numberButtons[i] = new JButton(String.valueOf(i + 1));
            numberPanel.add(numberButtons[i]);
            numberButtons[i].addActionListener(e -> {
                System.out.println("Clicked");
            });
        }
        numberPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        solveButton = new JButton("Solve");
        resetButton = new JButton("Reset");
        southPanel.add(solveButton);
        southPanel.add(resetButton);
        rightPanel.add(numberPanel, BorderLayout.CENTER);
        rightPanel.add(southPanel, BorderLayout.SOUTH);

        pane.add(leftPanel);
        pane.add(rightPanel);

        setVisible(true);
    }

    @Override
    public void update(SudokuCellUpdate cell) {
        // todo: update the GUI
    }

    @Override
    public void onRequestUserInput() {
        // todo: implement
    }

    @Override
    public void onSudokuFinished(int difficulty) {
        // todo: implement
    }

    @Override
    public void onInvalidMove(int value, int row, int col) {
        // todo: implement
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // todo: implement

        // use this.engine.receiveUserMove(new UserMove(row, col, value));
        // to send the user move to the engine
    }
}