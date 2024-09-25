package org.sudokusolver.Strategy;

import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Strategy.Regions.RegionManager;
public class SudokuSolver {
    RegionManager regionManager;

    public SudokuSolution findSudokuLevel(SudokuBoard sudoku) {
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }

        DifficultyLevel difficulty = DifficultyLevel.EASY;
        boolean isSolved = false;
        Solver solver;

        do {
            solver = new Solver(difficulty);
            solver.applySolver(regionManager);

            if (sudoku.isSolved()) {
                isSolved = true;
            } else {
                difficulty = difficulty.getNext();
            }

        } while(difficulty != null && !isSolved);

        regionManager = null;
        return new SudokuSolution(sudoku, difficulty);
    }
}
