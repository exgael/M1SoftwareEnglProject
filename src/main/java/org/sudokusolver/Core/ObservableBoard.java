package org.sudokusolver.Core;

import org.sudokusolver.Utils.Observer;
import org.sudokusolver.Utils.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class ObservableBoard<T> extends Board<T> implements Subject<BoardUpdate<T>> {
    private final List<Observer<BoardUpdate<T>>> observers;

    public ObservableBoard(int rows, int cols, Supplier<T> cellValueSupplier) {
        super(rows, cols, cellValueSupplier);
        this.observers = new ArrayList<>();
    }

    @Override
    public void setElement(int row, int col, T value) {
        T oldValue = getElement(row, col);
        super.setElement(row, col, value);
        notifyObservers(new BoardUpdate<>(row, col, oldValue, value));
    }

    @Override
    public void addObserver(Observer<BoardUpdate<T>> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<BoardUpdate<T>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(BoardUpdate<T> update) {
        for (Observer<BoardUpdate<T>> observer : observers) {
            observer.update(update);
        }
    }
}