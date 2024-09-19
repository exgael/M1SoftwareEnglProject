package org.example.Core;

public record BoardUpdate<T>(int row, int col, T oldValue, T newValue) {}