package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Gameplay.UserMove;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuGUI extends JFrame implements ActionListener, GameInterface {

    private final JButton startButton;
    private final JButton solveButton;
    private final JButton[] numberButtons;
    private final JLabel[][] cells;
    private final GameEngine engine;
    private int selectedValue = -1;


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
        for (int Row = 0; Row < 9; Row++) {
            for (int Col = 0; Col < 9; Col++) {
                final int row = Row;
                final int col = Col;

                cells[row][col] = new JLabel("");
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);

                // borders
                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : 1;
                int right = (col == 8) ? 2 : 1;

                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedValue != -1) {
                            cells[row][col].setText(String.valueOf(selectedValue));
                            engine.receiveUserMove(new UserMove(row, col, selectedValue));
                            selectedValue = -1;
                            for (JButton btn : numberButtons) {
                                btn.setEnabled(true);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select a value first");
                        }
                    }
                });

                sudokuPanel.add(cells[row][col]);
            }
        }
        sudokuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(sudokuPanel, BorderLayout.CENTER);

        //right side
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel numberPanel = new JPanel(new GridLayout(3, 3));
        numberButtons = new JButton[9];
        for (int I = 0; I < 9; I++) {
            final int i = I;
            numberButtons[i] = new JButton(String.valueOf(i + 1));
            int number = i + 1;
            numberButtons[i].addActionListener(e -> {
                for (JButton btn : numberButtons) {
                    btn.setEnabled(true);
                }
                selectedValue = number;
                numberButtons[i].setEnabled(false);
            });
            numberPanel.add(numberButtons[i]);
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
        //JOptionPane.showMessageDialog(this, "Le solveur ne peut pas continuer pour l'instant, veuillez entrer une valeur");
    }

    @Override
    public void onSudokuFinished(int difficulty) {
        // todo: implement
        JOptionPane.showMessageDialog(this, "Level difficulty is : "+levelRep(difficulty), "End of solver", JOptionPane.PLAIN_MESSAGE);
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
        // todo: implement
        //JOptionPane.showMessageDialog(this, "Invalid move");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // todo: implement
        // use this.engine.receiveUserMove(new UserMove(row, col, value));
        // to send the user move to the engine
    }
}