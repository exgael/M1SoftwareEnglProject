package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Gameplay.Sudoku.SudokuUpdate;

import javax.swing.*;

public class SudokuController implements GameInterface {
    private final GameEngine engine; // model
    private SudokuView sudokuView; // view
    private int selectedValue = -1;
    private boolean isGridLoaded = false;

    public SudokuController(GameEngine engine, SudokuView sudokuView) {
        this.sudokuView = sudokuView;
        this.engine = engine;
        this.engine.setGameInterface(this);
    }

    public void setSudokuView(SudokuView sudokuView) {
        this.sudokuView = sudokuView;
    }

    public void handleNumPadClick(int i) {
        SwingUtilities.invokeLater(() -> {
            // Reset any other selection
            sudokuView.resetPad();

            // Store selection as global
            selectedValue = i;

            // Once selected, the button of interest should be marked as such
            sudokuView.focusPadButton(i);
         });
    }

    public void handlePlayMove(int row, int col) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (selectedValue != -1) {
                    engine.receiveUserMove(row, col, selectedValue);
                    selectedValue = -1;
                    sudokuView.resetPad();
                } else {
                    JOptionPane.showMessageDialog(sudokuView, "Please select a value first");
                }
                return null;
            }
        }.execute();
    }

    public void handleSolveClicked(){
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                engine.play();
                return null;
            }
        }.execute();
    }

    public void handleLoadGridClick() {
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
                try {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    loadSudokuFromFilePath(filePath);
                    isGridLoaded = true; // Update status after loading grid
                    sudokuView.updateControlButtons(isGridLoaded); // Enable "Solve" and "Reset" buttons
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(sudokuView, e.getLocalizedMessage());
                }
            }
        } else if (choice == 1) {
            // Load from string
            String gridString = JOptionPane.showInputDialog(sudokuView, "0,1,2,3,...");
            if (gridString != null && !gridString.trim().isEmpty()) {
                try {
                    loadSudokuFromString(gridString);
                    isGridLoaded = true; // Update status after loading grid
                    sudokuView.updateControlButtons(isGridLoaded); // Enable "Solve" and "Reset" buttons
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(sudokuView, e.getLocalizedMessage());
                }
            }
        }
    }

    public void handleResetClicked() {
        engine.resetSudoku();
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

    @Override
    public void onRequestUserInput() {
        JOptionPane.showMessageDialog(sudokuView, "Help me!");
        SwingUtilities.invokeLater(sudokuView::enablePad);
    }

    @Override
    public void onSudokuFinished(int level) {
        JOptionPane.showMessageDialog(sudokuView, "Level difficulty is : "+levelRep(level), "End of solver", JOptionPane.PLAIN_MESSAGE);
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
        JOptionPane.showMessageDialog(sudokuView, "Conflicting values found! Game Over!");
    }

    @Override
    public void update(SudokuUpdate data) {
        SwingUtilities.invokeLater(() ->sudokuView.updateCell(data.row(), data.col(), data.value()));
    }
}