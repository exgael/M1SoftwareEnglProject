package org.sudokusolver.Strategy;

import org.sudokusolver.Gameplay.SudokuBoard;
import org.sudokusolver.Strategy.Regions.RegionManager;
public class SudokuSolver {
    RegionManager regionManager;

    public SudokuSolution findSudokuLevel(SudokuBoard sudoku) {
        DifficultyLevel difficulty = DifficultyLevel.EASY;
        if (regionManager == null) {
            this.regionManager = new RegionManager(sudoku);
        }

        while(difficulty != DifficultyLevel.EXPERT) {
            Solver solver = new Solver(difficulty);
            solver.applySolver(regionManager);
            if (sudoku.isSolved()) {
                regionManager = null;
                return new SudokuSolution(sudoku, difficulty);
            }
            difficulty = incrementDifficulty(difficulty);
        }

        regionManager = null;
        // return unsolved sudoku with expert level
        return new SudokuSolution(sudoku, DifficultyLevel.EXPERT);
    }

    private DifficultyLevel incrementDifficulty(DifficultyLevel difficulty) {
        switch (difficulty) {
            case EASY -> {
                return DifficultyLevel.MEDIUM;
            }
            case MEDIUM -> {
                return DifficultyLevel.HARD;
            }
            case HARD -> {
                return DifficultyLevel.EXPERT;
            }
            default -> throw new RuntimeException("Expert level not implemented yet");
        }
    }
}
