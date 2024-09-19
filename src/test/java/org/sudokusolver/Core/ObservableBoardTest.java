package org.sudokusolver.Core;

import org.sudokusolver.Utils.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

class ObservableBoardTest {

    private ObservableBoard<Integer> board;
    private final int ROWS = 3;
    private final int COLS = 4;

    private <T> Observer<BoardUpdate<T>> createMockObserver() {
        return  mock(new Observer<BoardUpdate<T>>() {
            @Override
            public void update(BoardUpdate<T> data) {

            }
        }.getClass());
    }

    @BeforeEach
    void setUp() {
        board = new ObservableBoard<>(ROWS, COLS, () -> 0);
    }

    @Test
    @DisplayName("ObservableBoard should be created with correct dimensions")
    void testBoardCreation() {
        assertEquals(ROWS, board.getRows());
        assertEquals(COLS, board.getCols());
    }

    @Test
    @DisplayName("ObservableBoard should be initialized with default values")
    void testBoardInitialization() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(0, board.getElement(row, col));
            }
        }
    }

    @Test
    @DisplayName("setValue should update cell value and notify observers")
    void testSetValue() {
        Observer<BoardUpdate<Integer>> mockObserver = createMockObserver();
        board.addObserver(mockObserver);

        board.setElement(1, 2, 5);

        assertEquals(5, board.getElement(1, 2));
        verify(mockObserver).update(new BoardUpdate<>(1, 2, 0, 5));
    }

    @Test
    @DisplayName("addObserver should add an observer")
    void testAddObserver() {
        Observer<BoardUpdate<Integer>> mockObserver = createMockObserver();

        // Add the observer
        board.addObserver(mockObserver);

        // Make a change to the board
        board.setElement(0, 0, 1);

        // Verify that the observer was notified
        @SuppressWarnings("unchecked")
        ArgumentCaptor<BoardUpdate<Integer>> updateCaptor = ArgumentCaptor.forClass(BoardUpdate.class);
        verify(mockObserver).update(updateCaptor.capture());

        BoardUpdate<Integer> capturedUpdate = updateCaptor.getValue();
        assertEquals(0, capturedUpdate.row());
        assertEquals(0, capturedUpdate.col());
        assertEquals(0, capturedUpdate.oldValue());
        assertEquals(1, capturedUpdate.newValue());
    }

    @Test
    @DisplayName("removeObserver should remove an observer")
    void testRemoveObserver() {
        Observer<BoardUpdate<Integer>> mockObserver = createMockObserver();
        board.addObserver(mockObserver);
        board.removeObserver(mockObserver);

        board.setElement(0, 0, 1);

        verify(mockObserver, never()).update(any(BoardUpdate.class));
    }

    @Test
    @DisplayName("notifyObservers should notify all observers")
    void testNotifyObservers() {
        Observer<BoardUpdate<Integer>> mockObserver1 = createMockObserver();
        Observer<BoardUpdate<Integer>> mockObserver2 = createMockObserver();
        board.addObserver(mockObserver1);
        board.addObserver(mockObserver2);

        board.setElement(1, 1, 5);

        verify(mockObserver1).update(new BoardUpdate<>(1, 1, 0, 5));
        verify(mockObserver2).update(new BoardUpdate<>(1, 1, 0, 5));
    }

    @Test
    @DisplayName("ObservableBoard should work with different types")
    void testGenericType() {
        ObservableBoard<String> stringBoard = new ObservableBoard<>(2, 2, () -> "empty");
        Observer<BoardUpdate<String>> mockObserver = createMockObserver();
        stringBoard.addObserver(mockObserver);

        stringBoard.setElement(1, 1, "full");

        assertEquals("empty", stringBoard.getElement(0, 0));
        assertEquals("full", stringBoard.getElement(1, 1));
        verify(mockObserver).update(new BoardUpdate<>(1, 1, "empty", "full"));
    }
}