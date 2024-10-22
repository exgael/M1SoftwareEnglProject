package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGUI extends JFrame implements ActionListener, GameInterface {

    private final JButton startButton;
    private final JButton solveButton;
    private final JButton[] numberButtons;
    private final JLabel[][] cells;
    private final GameEngine engine;

    public SudokuGUI(GameEngine engine) {
        super("Sudoku Solver");
        this.engine = engine;
        this.engine.setGameInterface(this);

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
                cells[row][col] = new JLabel("");
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
        startButton = new JButton("Start");
        solveButton = new JButton("Solve");
        startButton.addActionListener(this::whenStartClicked);
        solveButton.addActionListener(this::whenSolveClicked);
        southPanel.add(startButton);
        southPanel.add(solveButton);
        rightPanel.add(numberPanel, BorderLayout.CENTER);
        rightPanel.add(southPanel, BorderLayout.SOUTH);

        pane.add(leftPanel);
        pane.add(rightPanel);

        setVisible(true);

    }

    public void whenStartClicked(ActionEvent e) {
        String[] options = {"File Path", "String"};
        int choice = JOptionPane.showOptionDialog(this,
                "How do you want to load the Sudoku?",
                "Load Sudoku",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            // Load from file path
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                loadSudokuFromFilePath(filePath);
            }
        } else if (choice == 1) {
            // Load from string
            String gridString = JOptionPane.showInputDialog(this, "Enter Sudoku Grid (multiline):");
            loadSudokuFromString(gridString);
        }
    }

    private void loadSudokuFromFilePath(String filePath) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                engine.loadGridFromPath(filePath);
                return null;
            }
        }.execute();
    }

    private void loadSudokuFromString(String gridString) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                engine.loadGridFromString(gridString);
                return null;
            }
        }.execute();
    }

    public void whenSolveClicked(ActionEvent e){
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                engine.play();
                return null;
            }

            @Override
            protected void done() {
                // osef
            }

        }.execute();
    }

    @Override
    public void update(SudokuUpdate cell) {
        // todo: update the GUI
        SwingUtilities.invokeLater(() -> {
            System.out.println("Update");
            int row = cell.row();
            int col = cell.col();
            int value = cell.value();
            cells[row][col].setText(value == 0 ? "" : String.valueOf(value));
        });
    }

    @Override
    public void onRequestUserInput() {
        // todo: implement

    }

    @Override
    public void onSudokuFinished(int difficulty) {
        // todo: implement
        JOptionPane.showMessageDialog(this, "La difficulté est : "+levelRep(difficulty), "Fin du solveur", JOptionPane.PLAIN_MESSAGE);
    }

    private String levelRep(int dif) {
        return switch (dif) {
            case 0 -> " facile :)";
            case 1 -> " moyenne :|";
            case 2 -> " difficile :(";
            default -> " impossible x_x";
        };
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