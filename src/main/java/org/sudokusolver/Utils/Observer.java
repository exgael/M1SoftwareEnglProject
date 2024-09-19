package org.sudokusolver.Utils;

@FunctionalInterface
public interface Observer<T> {
    void update(T data);
}
