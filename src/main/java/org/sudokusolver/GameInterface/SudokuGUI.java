package org.sudokusolver.GameInterface;

import org.jetbrains.annotations.NotNull;
import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Gameplay.UserMove;
import org.sudokusolver.Strategy.Sudoku.SudokuUpdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuGUI extends JFrame implements GameInterface {

    private final JButton startButton;
    private final JButton solveButton;
    private final NumberPadView numberPad;
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
                var cellView = buildCellView(engine, Row, Col);
                sudokuPanel.add(cellView);
                cells[Row][Col] = cellView;
            }
        }
        sudokuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(sudokuPanel, BorderLayout.CENTER);

        //right side
        JPanel rightPanel = new JPanel(new BorderLayout());
        numberPad = new NumberPadView();
        numberPad.addNumberButtonListener(this::selectNumber);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButton = new JButton("Start");
        solveButton = new JButton("Solve");
        startButton.addActionListener(this::whenStartClicked);
        solveButton.addActionListener(this::whenSolveClicked);
        southPanel.add(startButton);
        southPanel.add(solveButton);
        rightPanel.add(numberPad, BorderLayout.CENTER);
        rightPanel.add(southPanel, BorderLayout.SOUTH);

        pane.add(leftPanel);
        pane.add(rightPanel);

        setVisible(true);

    }

    private void selectNumber(int i) {
        // Reset any other selection
        numberPad.resetButtons();

        // Store selection as global
        selectedValue = i;

        // Once selected, the button of interest should be marked as such
        numberPad.focusButton(i);
    }

    @NotNull
    private CellView buildCellView(GameEngine engine, int row, int col) {
        var cellView = new CellView(row, col);

        // borders
        int top = (row % 3 == 0) ? 2 : 1;
        int left = (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 2 : 1;
        int right = (col == 8) ? 2 : 1;

        cellView.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
        cellView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedValue != -1) {
                    cellView.setValue(selectedValue);
                    engine.receiveUserMove(new UserMove(row, col, selectedValue));
                    selectedValue = -1;
                    numberPad.resetButtons();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a value first");
                }
            }
        });
        return cellView;
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
}