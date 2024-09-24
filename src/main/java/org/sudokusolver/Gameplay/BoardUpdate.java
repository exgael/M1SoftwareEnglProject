package org.sudokusolver.Gameplay;

public record BoardUpdate<T>(int row, int col, T oldValue, T newValue) {
}