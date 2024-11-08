package org.sudokusolver.GameInterface;

import org.sudokusolver.Gameplay.GameEngine;
import org.sudokusolver.Gameplay.GameInterface;
import org.sudokusolver.Gameplay.UserMove;
import org.sudokusolver.Gameplay.Sudoku.SudokuUpdate;

import javax.swing.*;

public class SudokuController implements GameInterface {
    private final GameEngine engine; // model
    private SudokuView sudokuView; // view
    private int selectedValue = -1;

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
                    engine.receiveUserMove(new UserMove(row, col, selectedValue));
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

    public void handleStartClicked() {
        // Load from string
        String gridString = JOptionPane.showInputDialog(sudokuView, "0,1,2,3,...");
        loadSudokuFromString(gridString);
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