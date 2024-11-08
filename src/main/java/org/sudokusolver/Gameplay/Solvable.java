package org.sudokusolver.Gameplay;

import org.sudokusolver.Gameplay.Sudoku.Modifiable;
import org.sudokusolver.Gameplay.Sudoku.Regions.Region;

import java.util.stream.Stream;

public interface Solvable extends Modifiable {
    boolean isSolved();
    Stream<Region> streamRegions();
}
