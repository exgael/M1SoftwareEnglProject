package org.sudokusolver.Gameplay;

@FunctionalInterface
public interface Loadable {
    void load(int[] grid);
}
