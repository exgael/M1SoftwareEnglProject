package org.sudokusolver.Strategy;

public enum DifficultyLevel {
    EASY,
    MEDIUM,
    HARD;

    /**
     * Get the next difficulty level
     * <a href="https://stackoverflow.com/questions/17664445/is-there-an-increment-operator-for-java-enum">Source</a>
     * @return the next difficulty level
     */
    public DifficultyLevel getNext() {
        if (ordinal() == values().length - 1)
            return null;
        return values()[ordinal() + 1];
    }
}
