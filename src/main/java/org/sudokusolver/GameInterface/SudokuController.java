package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.UserMove;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SudokuController {

    private final SudokuView sudokuView;
    private final GameEngine engine;
    private int selectedValue = -1;

    public SudokuController(SudokuView sudokuView, GameEngine engine) {
        this.sudokuView = sudokuView;
        this.engine = engine;
        this.engine.setGameInterface(sudokuView);

        sudokuView.addCellClickListener(this::handleCellClick);
        sudokuView.addNumpadClickListener(this::handleNumPadClick);
        sudokuView.addStartButtonListener(this::whenStartClicked);
        sudokuView.addSolveButtonListener(this::whenSolveClicked);
    }

    public void handleNumPadClick(int i) {
        // Reset any other selection
        sudokuView.resetPad();

        // Store selection as global
        selectedValue = i;

        // Once selected, the button of interest should be marked as such
        sudokuView.focusPadButton(i);
    }

    public void handleCellClick(int row, int col) {
        if (selectedValue != -1) {
            sudokuView.updateCell(row, col, selectedValue);
            engine.receiveUserMove(new UserMove(row, col, selectedValue));
            selectedValue = -1;
            sudokuView.resetPad();
        } else {
            JOptionPane.showMessageDialog(sudokuView, "Please select a value first");
        }
    }

    public void whenStartClicked(ActionEvent e) {
        String[] options = {"File Path", "String"};
        int choice = JOptionPane.showOptionDialog(sudokuView,
                "How do you want to load the Sudoku?",
                "Load Sudoku",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            // Load from file path
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(sudokuView);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                loadSudokuFromFilePath(filePath);
            }
        } else if (choice == 1) {
            // Load from string
            String gridString = JOptionPane.showInputDialog(sudokuView, "0,1,2,3,...");
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
}