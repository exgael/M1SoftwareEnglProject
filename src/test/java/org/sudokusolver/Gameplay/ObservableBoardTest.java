package org.sudokusolver.Gameplay;

import org.sudokusolver.Utils.Inspectable;
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

    static class MockCell implements Inspectable {
        private int value;

        public MockCell(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String debugDescription() {
            return Integer.toString(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            MockCell other = (MockCell) obj;
            return value == other.value;
        }
    }

    private ObservableBoard<MockCell> board;
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
        board = new ObservableBoard<>(ROWS, COLS, () -> new MockCell(0));
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
                assertEquals(new MockCell(0), board.getElement(row, col));
            }
        }
    }

    @Test
    @DisplayName("setValue should update cell value and notify observers")
    void testSetValue() {
        Observer<BoardUpdate<MockCell>> mockObserver = createMockObserver();
        board.addObserver(mockObserver);

        board.setElement(1, 2, new MockCell(5));

        assertEquals(new MockCell(5), board.getElement(1, 2));
        verify(mockObserver).update(new BoardUpdate<MockCell>(1, 2, new MockCell(0), new MockCell(5)));
    }

    @Test
    @DisplayName("addObserver should add an observer")
    void testAddObserver() {
        Observer<BoardUpdate<MockCell>> mockObserver = createMockObserver();

        // Add the observer
        board.addObserver(mockObserver);

        // Make a change to the board
        board.setElement(0, 0, new MockCell(1));

        // Verify that the observer was notified
        @SuppressWarnings("unchecked")
        ArgumentCaptor<BoardUpdate<MockCell>> updateCaptor = ArgumentCaptor.forClass(BoardUpdate.class);
        verify(mockObserver).update(updateCaptor.capture());

        BoardUpdate<MockCell> capturedUpdate = updateCaptor.getValue();
        assertEquals(0, capturedUpdate.row());
        assertEquals(0, capturedUpdate.col());
        assertEquals(new MockCell(0), capturedUpdate.oldValue());
        assertEquals(new MockCell(1), capturedUpdate.newValue());
    }

    @Test
    @DisplayName("removeObserver should remove an observer")
    void testRemoveObserver() {
        Observer<BoardUpdate<MockCell>> mockObserver = createMockObserver();
        board.addObserver(mockObserver);
        board.removeObserver(mockObserver);

        board.setElement(0, 0, new MockCell(1));

        verify(mockObserver, never()).update(any(BoardUpdate.class));
    }

    @Test
    @DisplayName("notifyObservers should notify all observers")
    void testNotifyObservers() {
        Observer<BoardUpdate<MockCell>> mockObserver1 = createMockObserver();
        Observer<BoardUpdate<MockCell>> mockObserver2 = createMockObserver();
        board.addObserver(mockObserver1);
        board.addObserver(mockObserver2);

        board.setElement(1, 1, new MockCell(5));

        verify(mockObserver1).update(new BoardUpdate<>(1, 1, new MockCell(0), new MockCell(5)));
        verify(mockObserver2).update(new BoardUpdate<>(1, 1, new MockCell(0), new MockCell(5)));
    }
}