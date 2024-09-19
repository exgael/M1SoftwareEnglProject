package org.example.Utils;

@FunctionalInterface
public interface Observer<T> {
    void update(T data);
}
