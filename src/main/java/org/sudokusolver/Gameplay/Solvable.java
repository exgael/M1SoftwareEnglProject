package org.sudokusolver.Gameplay;

import org.sudokusolver.Strategy.Sudoku.Regions.Region;

import java.util.stream.Stream;

public interface Solvable extends Modifiable {
    boolean isSolved();
    Stream<Region> streamRegions();
}
